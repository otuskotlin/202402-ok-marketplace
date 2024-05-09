package ru.otus.otuskotlin.marketplace.backend.repo.tests

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseErr
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoAdReadTest {
    abstract val repo: IRepoAd
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(readSucc.id))

        assertIs<DbAdResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readAd(DbAdIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbAdResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: MkplError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("read") {
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MkplAdId("ad-repo-read-notFound")

    }
}
