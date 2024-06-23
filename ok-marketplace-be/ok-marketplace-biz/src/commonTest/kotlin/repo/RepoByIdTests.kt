package repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.backend.repo.tests.AdRepositoryMock
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import ru.otus.otuskotlin.marketplace.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initAd = MkplAd(
    id = MkplAdId("123"),
    title = "abc",
    description = "abc",
    adType = MkplDealSide.DEMAND,
    visibility = MkplVisibility.VISIBLE_PUBLIC,
)
private val repo = AdRepositoryMock(
        invokeReadAd = {
            if (it.id == initAd.id) {
                DbAdResponseOk(
                    data = initAd,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = MkplCorSettings(repoTest = repo)
private val processor = MkplAdProcessor(settings)

fun repoNotFoundTest(command: MkplCommand) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAd(
            id = MkplAdId("12345"),
            title = "xyz",
            description = "xyz",
            adType = MkplDealSide.DEMAND,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = MkplAdLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(MkplState.FAILING, ctx.state)
    assertEquals(MkplAd(), ctx.adResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
