package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.repo.postgresql.SqlFields.quoted
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.repo.common.IRepoAdInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoAdSql actual constructor(
    properties: SqlProperties,
    val randomUuid: () -> String,
) : IRepoAd, IRepoAdInitializable {
    init {
        require(properties.database.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.schema.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL schema must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.table.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL table must contain only letters, numbers and underscore symbol '_'"
        }
    }

    private val dbName: String = "\"${properties.schema}\".\"${properties.table}\"".apply {
        // Валидируем, что админ не ошибся в имени таблицы
    }
    private val driver by lazy {
        PostgresDriver(
            host = properties.host,
            port = properties.port,
            user = properties.user,
            database = properties.database,
            password = properties.password,
        )
    }

    private suspend fun saveElement(saveAd: MkplAd): IDbAdResponse {
        val sql = """
                INSERT INTO $dbName (
                  ${SqlFields.ID.quoted()}, 
                  ${SqlFields.TITLE.quoted()}, 
                  ${SqlFields.DESCRIPTION.quoted()},
                  ${SqlFields.VISIBILITY.quoted()},
                  ${SqlFields.AD_TYPE.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()},
                  ${SqlFields.PRODUCT_ID.quoted()}
                ) VALUES (
                  :${SqlFields.ID}, 
                  :${SqlFields.TITLE}, 
                  :${SqlFields.DESCRIPTION}, 
                  :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE}, 
                  :${SqlFields.AD_TYPE}::${SqlFields.AD_TYPE_TYPE}, 
                  :${SqlFields.LOCK}, 
                  :${SqlFields.OWNER_ID}, 
                  :${SqlFields.PRODUCT_ID}
                )
                RETURNING ${SqlFields.allFields.joinToString()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            saveAd.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbAdResponseOk(res.first())
    }

    actual override fun save(ads: Collection<MkplAd>): Collection<MkplAd> = runBlocking {
        ads.map {
            val res = saveElement(it)
            if (res !is DbAdResponseOk) throw Exception()
            res.data
        }
    }

    actual override suspend fun createAd(rq: DbAdRequest): IDbAdResponse {
        val saveAd = rq.ad.copy(id = MkplAdId(randomUuid()))
        return saveElement(saveAd)
    }

    actual override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse {
        val sql = """
                SELECT 
                  ${SqlFields.ID.quoted()}, 
                  ${SqlFields.TITLE.quoted()}, 
                  ${SqlFields.DESCRIPTION.quoted()},
                  ${SqlFields.VISIBILITY.quoted()},
                  ${SqlFields.AD_TYPE.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()},
                  ${SqlFields.PRODUCT_ID.quoted()}
                FROM $dbName
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID.quoted()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbAdResponseOk(res.first())
    }

    actual override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse {
        TODO("Not yet implemented")
    }

    actual fun clear() = runBlocking {
        val sql = """
                DELETE FROM $dbName 
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
    }
}
