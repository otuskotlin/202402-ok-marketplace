package ru.otus.otuskotlin.marketplace.app.tmp

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.log1.mapper.toLog
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback


suspend fun main() {
    val logger = mpLoggerLogback("app-tmp")
    while (true) {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            state = MkplState.RUNNING,
            workMode = MkplWorkMode.STUB,
            timeStart = Clock.System.now(),
            requestId = MkplRequestId("tmp-request"),
            adRequest = MkplAd(
                title = "tmp title",
                description = "tmp desc",
                adType = MkplDealSide.DEMAND,
                visibility = MkplVisibility.VISIBLE_PUBLIC,
            ),
            adResponse = MkplAd(
                title = "tmp title",
                description = "tmp desc",
                adType = MkplDealSide.DEMAND,
                visibility = MkplVisibility.VISIBLE_PUBLIC,
                ownerId = MkplUserId("tmp-user-id"),
                lock = MkplAdLock("tmp-lock"),
                permissionsClient = mutableSetOf(MkplAdPermissionClient.READ, MkplAdPermissionClient.UPDATE),
            ),
            errors = mutableListOf(
                MkplError(
                    code = "tmp-error",
                    group = "tmp",
                    field = "none",
                    message = "tmp error message",
                    level = LogLevel.INFO,
                    exception = Exception("some exception"),
                ),
            )
        )
        logger.info(
            msg = "tmp log string",
            data = ctx.toLog("tmp-app-logg"),
        )
        delay(500)
    }
}
