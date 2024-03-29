package ru.otus.otuskotlin.marketplace.app.ktor

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.app.ktor.v2.v2Ad
import ru.otus.otuskotlin.marketplace.app.ktor.plugins.initAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.v2.wsHandlerV2

fun Application.module(
    appSettings: MkplAppSettings = initAppSettings()
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        anyHost()
    }
    install(WebSockets)

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
            v2Ad(appSettings)
            webSocket("/ws") {
                wsHandlerV2(appSettings)
            }
        }
    }
}
