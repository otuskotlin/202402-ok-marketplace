package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

expect fun Application.getLoggerProviderConf(): MpLoggerProvider
