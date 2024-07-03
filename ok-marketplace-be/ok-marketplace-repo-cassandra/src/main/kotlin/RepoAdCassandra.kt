package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdCassandraDTO
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdDealSide
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdVisibility
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplAdLock
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.repo.common.IRepoAdInitializable
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RepoAdCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<MkplAd> = emptyList(),
) : AdRepoBase(), IRepoAd, IRepoAdInitializable {
    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(AdVisibility::class.java))
            register(EnumNameCodec(AdDealSide::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(AdCassandraDTO.table(keyspace, AdCassandraDTO.TABLE_NAME))
        session.execute(AdCassandraDTO.titleIndex(keyspace, AdCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.adDao(keyspaceName, AdCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(AdCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    override fun save(ads: Collection<MkplAd>): Collection<MkplAd> = ads.onEach { dao.create(AdCassandraDTO(it)) }

    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val new = rq.ad.copy(id = MkplAdId(randomUuid()), lock = MkplAdLock(randomUuid()))
        dao.create(AdCassandraDTO(new)).await()
        DbAdResponseOk(new)
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        if (rq.id == MkplAdId.NONE) return@tryAdMethod errorEmptyId
        val res = dao.read(rq.id.asString()).await() ?: return@tryAdMethod errorNotFound(rq.id)
        DbAdResponseOk(res.toAdModel())
    }

    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val idStr = rq.ad.id.asString()
        val prevLock = rq.ad.lock.asString()
        val new = rq.ad.copy(lock = MkplAdLock(randomUuid()))
        val dto = AdCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(AdCassandraDTO.COLUMN_LOCK) }
            ?.getString(AdCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbAdResponseOk(new)
            // res.wasApplied() -> DbAdResponse.success(dao.read(idStr).await()?.toAdModel())
            resultField == null -> errorNotFound(rq.ad.id)
            else -> errorRepoConcurrency(
                oldAd = dao.read(idStr).await()?.toAdModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.ad.lock
            )
        }
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val idStr = rq.id.asString()
        val prevLock = rq.lock.asString()
        val oldAd = dao.read(idStr).await()?.toAdModel() ?: return@tryAdMethod errorNotFound(rq.id)
        val res = dao.delete(idStr, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(AdCassandraDTO.COLUMN_LOCK) }
            ?.getString(AdCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbAdResponseOk(oldAd)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(idStr).await()?.toAdModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse = tryAdsMethod {
        val found = dao.search(rq).await()
        DbAdsResponseOk(found.map { it.toAdModel() })
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}
