package ru.otus.otuskotlin.marketplace.backend.repo.sql

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplAdLock
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd

class RepoAdSql(
    properties: SqlProperties,
    initObjects: Collection<MkplAd> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IRepoAd {
    val driver = PostgresDriver(
        host = "host.docker.internal",
        port = 5432,
        user = "postgres",
        database = "postgres",
        password = "postgres",
    )
//    private val adTable = AdTable(properties.table)
//
//    private val driver = when {
//        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
//        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
//    }
//
//    private val conn = Database.connect(
//        properties.url, driver, properties.user, properties.password
//    )
//
//
//    init {
//        transaction(conn) {
//            SchemaUtils.create(adTable)
//            initObjects.forEach { createAd(it) }
//        }
//    }
//
//    private fun createAd(ad: MkplAd): MkplAd {
//        val res = adTable
//            .insert {
//                to(it, ad, randomUuid)
//            }
//            .resultedValues
//            ?.map { adTable.from(it) }
//        return res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
//    }
//
//    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
//        try {
//            transaction(conn) {
//                block()
//            }
//        } catch (e: Exception) {
//            handle(e)
//        }
//
//    private fun transactionWrapper(block: () -> DbAdResponse): DbAdResponse =
//        transactionWrapper(block) { DbAdResponse.error(it.asMkplError()) }
//
//    override suspend fun createAd(rq: DbAdRequest): DbAdResponse = transactionWrapper {
//        DbAdResponse.success(createAd(rq.ad))
//    }
//
//    private fun read(id: MkplAdId): DbAdResponse {
//        val res = adTable.select {
//            adTable.id eq id.asString()
//        }.singleOrNull() ?: return DbAdResponse.errorNotFound
//        return DbAdResponse.success(adTable.from(res))
//    }
//
//    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse = transactionWrapper { read(rq.id) }
//
//    private fun update(
//        id: MkplAdId,
//        lock: MkplAdLock,
//        block: (MkplAd) -> DbAdResponse
//    ): DbAdResponse =
//        transactionWrapper {
//            if (id == MkplAdId.NONE) return@transactionWrapper DbAdResponse.errorEmptyId
//
//            val current = adTable.select { adTable.id eq id.asString() }
//                .singleOrNull()
//                ?.let { adTable.from(it) }
//
//            when {
//                current == null -> DbAdResponse.errorNotFound
//                current.lock != lock -> DbAdResponse.errorConcurrent(lock, current)
//                else -> block(current)
//            }
//        }
//
//
//    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse = update(rq.ad.id, rq.ad.lock) {
//        adTable.update({ adTable.id eq rq.ad.id.asString() }) {
//            to(it, rq.ad.copy(lock = MkplAdLock(randomUuid())), randomUuid)
//        }
//        read(rq.ad.id)
//    }
//
//    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse = update(rq.id, rq.lock) {
//        adTable.deleteWhere { id eq rq.id.asString() }
//        DbAdResponse.success(it)
//    }
//
//    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse =
//        transactionWrapper({
//            val res = adTable.select {
//                buildList {
//                    add(Op.TRUE)
//                    if (rq.ownerId != MkplUserId.NONE) {
//                        add(adTable.owner eq rq.ownerId.asString())
//                    }
//                    if (rq.dealSide != MkplDealSide.NONE) {
//                        add(adTable.dealSide eq rq.dealSide)
//                    }
//                    if (rq.titleFilter.isNotBlank()) {
//                        add(
//                            (adTable.title like "%${rq.titleFilter}%")
//                                    or (adTable.description like "%${rq.titleFilter}%")
//                        )
//                    }
//                }.reduce { a, b -> a and b }
//            }
//            DbAdsResponse(data = res.map { adTable.from(it) }, isSuccess = true)
//        }, {
//            DbAdsResponse.error(it.asMkplError())
//        })
}
