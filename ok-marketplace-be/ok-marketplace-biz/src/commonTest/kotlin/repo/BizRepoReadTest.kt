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

class BizRepoReadTest {

    private val userId = MkplAdStubBolts.AD_DEMAND_BOLT1.ownerId
    private val command = MkplCommand.READ
    private val initAd = MkplAd(
        id = MkplAdId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
    )
    private val repo = AdRepositoryMock(
        invokeReadAd = {
            DbAdResponseOk(
                data = initAd,
            )
        }
    )
    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAd(
                id = MkplAdId("123"),
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.adType, ctx.adResponse.adType)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
