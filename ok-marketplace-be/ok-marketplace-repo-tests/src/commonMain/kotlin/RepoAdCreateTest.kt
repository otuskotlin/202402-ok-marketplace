package ru.otus.otuskotlin.marketplace.backend.repo.tests

import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals


abstract class RepoAdCreateTest {
    abstract val repo: IRepoAd
    protected open val lockNew: MkplAdLock = MkplAdLock("20000000-0000-0000-0000-000000000002")

    private val createObj = MkplAd(
        title = "create object",
        description = "create object description",
        ownerId = MkplUserId("owner-123"),
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
        adType = MkplDealSide.SUPPLY,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createAd(DbAdRequest(createObj))
        assertIs<DbAdResponseOk>(result)
        val expected = createObj.copy(id = result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.adType, result.data.adType)
        assertNotEquals(MkplAdId.NONE, result.data.id)
    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<MkplAd> = emptyList()
    }
}
