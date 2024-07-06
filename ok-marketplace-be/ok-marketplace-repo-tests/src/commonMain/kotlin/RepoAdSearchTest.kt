package ru.otus.otuskotlin.marketplace.backend.repo.tests

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponseOk
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoAdSearchTest {
    abstract val repo: AdRepoInitialized

    protected open val initializedObjects: List<MkplAd> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(ownerId = searchOwnerId))
        assertIs<DbAdsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchDealSide() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(dealSide = MkplDealSide.SUPPLY))
        assertIs<DbAdsResponseOk>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitAds("search") {

        val searchOwnerId = MkplUserId("owner-124")
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", adType = MkplDealSide.SUPPLY),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5", adType = MkplDealSide.SUPPLY),
        )
    }
}
