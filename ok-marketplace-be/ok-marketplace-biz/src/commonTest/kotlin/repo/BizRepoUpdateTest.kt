package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.marketplace.backend.repo.tests.AdRepositoryMock
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubBolts
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = MkplAdStubBolts.AD_DEMAND_BOLT1.ownerId
    private val command = MkplCommand.UPDATE
    private val initAd = MkplAd(
        id = MkplAdId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
        lock = MkplAdLock("123-234-abc-ABC"),
    )
    private val repo = AdRepositoryMock(
        invokeReadAd = {
            DbAdResponseOk(
                data = initAd,
            )
        },
        invokeUpdateAd = {
            DbAdResponseOk(
                data = MkplAd(
                    id = MkplAdId("123"),
                    title = "xyz",
                    description = "xyz",
                    adType = MkplDealSide.DEMAND,
                    visibility = MkplVisibility.VISIBLE_TO_GROUP,
                    lock = MkplAdLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = MkplAd(
            id = MkplAdId("123"),
            title = "xyz",
            description = "xyz",
            adType = MkplDealSide.DEMAND,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = MkplAdLock("123-234-abc-ABC"),
        )
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = adToUpdate,
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.adResponse.id)
        assertEquals(adToUpdate.title, ctx.adResponse.title)
        assertEquals(adToUpdate.description, ctx.adResponse.description)
        assertEquals(adToUpdate.adType, ctx.adResponse.adType)
        assertEquals(adToUpdate.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
