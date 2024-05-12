package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.test.runTest
import platform.posix.getenv
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import kotlin.test.Test

class RepoAdSqlTest {
    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun create() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoAdSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.createAd(
            rq = DbAdRequest(
                MkplAd(
                    title = "tttt",
                    description = "zzzz",
                    visibility = MkplVisibility.VISIBLE_PUBLIC,
                    adType = MkplDealSide.DEMAND,
                    ownerId = MkplUserId("1234"),
                    productId = MkplProductId("4567"),
                    lock = MkplAdLock("235356"),
                )
            )
        )
        println("CREATED $res")
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun search() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoAdSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.searchAd(
            rq = DbAdFilterRequest(
                titleFilter = "tttt",
                dealSide = MkplDealSide.DEMAND,
                ownerId = MkplUserId("1234"),
            )
        )
        println("SEARCH $res")
    }
}
