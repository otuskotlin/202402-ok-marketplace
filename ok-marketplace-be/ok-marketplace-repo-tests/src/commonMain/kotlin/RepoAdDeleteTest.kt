package ru.otus.otuskotlin.marketplace.backend.repo.tests

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoAdDeleteTest {
    abstract val repo: IRepoAd
    protected open val deleteSucc = initObjects[0]
    protected open val notFoundId = MkplAdId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteAd(DbAdIdRequest(deleteSucc.id))
        assertIs<DbAdResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(notFoundId))

        assertIs<DbAdResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    companion object : BaseInitAds("delete") {
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("delete"),
        )
    }
}
