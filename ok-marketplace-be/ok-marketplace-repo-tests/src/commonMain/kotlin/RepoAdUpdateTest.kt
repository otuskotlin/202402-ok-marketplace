package ru.otus.otuskotlin.marketplace.backend.repo.tests

import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoAdUpdateTest {
    abstract val repo: IRepoAd
    protected open val updateSucc = initObjects[0]
    protected val updateIdNotFound = MkplAdId("ad-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        MkplAd(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = MkplUserId("owner-123"),
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            adType = MkplDealSide.SUPPLY,
        )
    }
    private val reqUpdateNotFound = MkplAd(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MkplUserId("owner-123"),
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
        adType = MkplDealSide.SUPPLY,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateSucc))
        assertIs<DbAdResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.adType, result.data.adType)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateNotFound))
        assertIs<DbAdResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("update"),
        )
    }
}
