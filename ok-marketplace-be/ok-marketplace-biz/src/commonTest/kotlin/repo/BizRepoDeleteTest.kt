package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.marketplace.backend.repo.tests.AdRepositoryMock
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseErr
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.DELETE
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
        },
        invokeDeleteAd = {
            if (it.id == initAd.id)
                DbAdResponseOk(
                    data = initAd
                )
            else DbAdResponseErr()
        }
    )
    private val settings by lazy {
        MkplCorSettings(
            repoTest = repo
        )
    }
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = MkplAd(
            id = MkplAdId("123"),
            lock = MkplAdLock("123"),
        )
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.adType, ctx.adResponse.adType)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
