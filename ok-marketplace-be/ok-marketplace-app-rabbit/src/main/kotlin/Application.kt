package ru.otus.otuskotlin.marketplace.app.rabbit

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.marketplace.app.rabbit.mappers.fromArgs
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback

fun main(vararg args: String) = runBlocking {
    val appSettings = MkplAppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = MkplCorSettings(
            loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}
