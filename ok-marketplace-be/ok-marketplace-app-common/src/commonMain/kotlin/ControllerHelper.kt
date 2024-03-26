package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.log1.mapper.toLog
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import kotlin.reflect.KClass

suspend inline fun <T> IMkplAppSettings.controllerHelper(
    crossinline getRequest: suspend MkplContext.() -> Unit,
    crossinline toResponse: suspend MkplContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = MkplState.FAILING
        ctx.errors.add(e.asMkplError())
        processor.exec(ctx)
        if (ctx.command == MkplCommand.NONE) {
            ctx.command = MkplCommand.READ
        }
        ctx.toResponse()
    }
}
