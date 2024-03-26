package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.kermit.mpLoggerKermit

actual fun Application.getLoggerProviderConf(): MpLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "socket", "sock" -> getSocketLoggerProvider()
        "kmp", null -> MpLoggerProvider { mpLoggerKermit(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and socket")
    }
