package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import com.benasher44.uuid.uuid4
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_AD_TYPE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_LOCK
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_OWNER_ID
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_TITLE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.RESULT_LOCK_FAILURE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.*
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.common.repo.exceptions.UnknownDbException
import ru.otus.otuskotlin.marketplace.repo.common.IRepoAdInitializable
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr


class AdRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "",
    private val graph: String = "graph", // Этот граф должен быть настроен в /home/arcadedb/config/gremlin-server.groovy
    val randomUuid: () -> String = { uuid4().toString() },
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
) : AdRepoBase(), IRepoAd, IRepoAdInitializable, AutoCloseable {

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster, graph)) }

    init {
        initRepo?.also { it(g) }
    }

    private fun save(ad: MkplAd): MkplAd = g.addV(ad.label())
        .addMkplAd(ad)
        .listMkplAd()
        .next()
        ?.toMkplAd()
        ?: throw RuntimeException("Cannot initialize object $ad")

    override fun save(ads: Collection<MkplAd>): Collection<MkplAd> = ads.map { save(it) }

    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val ad = rq.ad.copy(lock = MkplAdLock(randomUuid()))
        g.addV(ad.label())
            .addMkplAd(ad)
            .listMkplAd()
            .next()
            ?.toMkplAd()
            ?.let { DbAdResponseOk(it) }
            ?: errorDb(UnknownDbException("Db object was not returned after creation by DB: $rq"))
    }

    private suspend fun <T : Any> checkNotFound(block: suspend () -> T?): T? = runCatching { block() }.getOrElse { e ->
        if (e.cause is ResponseException) null else throw e
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return@tryAdMethod errorEmptyId
        val adResp = checkNotFound { g.V(key).listMkplAd().next()?.toMkplAd() }
        when {
            adResp == null -> errorNotFound(rq.id)
            else -> DbAdResponseOk(adResp)
        }
    }

    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val key = rq.ad.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return@tryAdMethod errorEmptyId
        val oldLock = rq.ad.lock.takeIf { it != MkplAdLock.NONE } ?: return@tryAdMethod errorEmptyLock(rq.ad.id)
        val newLock = MkplAdLock(randomUuid())
        val newAd = rq.ad.copy(lock = newLock)
        val adResp = checkNotFound {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Any>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a").addMkplAd(newAd).listMkplAd(),
                    gr.select<Vertex, Vertex>("a").listMkplAd(result = RESULT_LOCK_FAILURE)
                )
                .next()
                ?.toMkplAd()
        }
        when {
            adResp == null -> errorNotFound(rq.ad.id)
            adResp.lock == newAd.lock -> DbAdResponseOk(adResp)
            else -> errorRepoConcurrency(adResp, oldLock)
        }
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return@tryAdMethod errorEmptyId
        val oldLock = rq.lock.takeIf { it != MkplAdLock.NONE } ?: return@tryAdMethod errorEmptyLock(rq.id)
        val res = checkNotFound {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Vertex>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>())
                        .listMkplAd(),
                    gr.select<Vertex, Vertex>("a")
                        .listMkplAd(result = RESULT_LOCK_FAILURE)
                )
                .next()
        }
        val adResp = res?.toMkplAd()
        when {
            adResp == null -> errorNotFound(rq.id)
            res.getFailureMarker() == RESULT_LOCK_FAILURE -> errorRepoConcurrency(adResp, oldLock)
            else -> DbAdResponseOk(adResp)
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse = tryAdsMethod {
        g.V()
            .apply { rq.ownerId.takeIf { it != MkplUserId.NONE }?.also { has(FIELD_OWNER_ID, it.asString()) } }
            .apply { rq.dealSide.takeIf { it != MkplDealSide.NONE }?.also { has(FIELD_AD_TYPE, it.name) } }
            .apply {
                rq.titleFilter.takeIf { it.isNotBlank() }?.also { has(FIELD_TITLE, TextP.containing(it)) }
            }
            .listMkplAd()
            .toList()
            .let { result -> DbAdsResponseOk(data = result.map { it.toMkplAd() }) }
    }

    override fun close() {
        if ((::g::getDelegate as? Lazy<*>)?.isInitialized() == true) {
            g.close()
        }
        if ((::cluster::getDelegate as? Lazy<*>)?.isInitialized() == true) {
            cluster.close()
        }
    }

}
