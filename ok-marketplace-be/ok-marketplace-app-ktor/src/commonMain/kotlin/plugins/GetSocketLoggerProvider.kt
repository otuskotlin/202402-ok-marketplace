package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.socket.SocketLoggerSettings
import ru.otus.otuskotlin.marketplace.logging.socket.mpLoggerSocket

fun Application.getSocketLoggerProvider(): MpLoggerProvider {
    val loggerSettings = environment.config.config("ktor.socketLogger").let { conf ->
        SocketLoggerSettings(
            host = conf.propertyOrNull("host")?.getString() ?: "127.0.0.1",
            port = conf.propertyOrNull("port")?.getString()?.toIntOrNull() ?: 9002,
        )
    }
    return MpLoggerProvider { mpLoggerSocket(it, loggerSettings) }
}
