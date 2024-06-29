package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.repo.common.IRepoAdInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoAdSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoAd, IRepoAdInitializable {
    private val adTable = AdTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        adTable.deleteAll()
    }

    private fun saveObj(ad: MkplAd): MkplAd = transaction(conn) {
        val res = adTable
            .insert {
                to(it, ad, randomUuid)
            }
            .resultedValues
            ?.map { adTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbAdResponse): IDbAdResponse =
        transactionWrapper(block) { DbAdResponseErr(it.asMkplError()) }

    actual override fun save(ads: Collection<MkplAd>): Collection<MkplAd> = ads.map { saveObj(it) }
    actual override suspend fun createAd(rq: DbAdRequest): IDbAdResponse = transactionWrapper {
        DbAdResponseOk(saveObj(rq.ad))
    }

    private fun read(id: MkplAdId): IDbAdResponse {
        val res = adTable.selectAll().where {
            adTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbAdResponseOk(adTable.from(res))
    }

    actual override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: MkplAdId,
        lock: MkplAdLock,
        block: (MkplAd) -> IDbAdResponse
    ): IDbAdResponse =
        transactionWrapper {
            if (id == MkplAdId.NONE) return@transactionWrapper errorEmptyId

            val current = adTable.selectAll().where { adTable.id eq id.asString() }
                .singleOrNull()
                ?.let { adTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    actual override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse = update(rq.ad.id, rq.ad.lock) {
        adTable.update({ adTable.id eq rq.ad.id.asString() }) {
            to(it, rq.ad.copy(lock = MkplAdLock(randomUuid())), randomUuid)
        }
        read(rq.ad.id)
    }

    actual override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse = update(rq.id, rq.lock) {
        adTable.deleteWhere { id eq rq.id.asString() }
        DbAdResponseOk(it)
    }

    actual override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse =
        transactionWrapper({
            val res = adTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != MkplUserId.NONE) {
                        add(adTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.dealSide != MkplDealSide.NONE) {
                        add(adTable.dealSide eq rq.dealSide)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (adTable.title like "%${rq.titleFilter}%")
                                    or (adTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbAdsResponseOk(data = res.map { adTable.from(it) })
        }, {
            DbAdsResponseErr(it.asMkplError())
        })
}
