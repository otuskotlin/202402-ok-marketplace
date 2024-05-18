package ru.otus.otuskotlin.marketplace.blackbox.fixture.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

/**
 * Отправка запросов по http/websocket
 */
class WebSocketClient(dockerCompose: DockerCompose) : Client {
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            protocol = URLProtocol.WS
            path("ws/$version")
        }.build().toString()

        var response = ""
        client.webSocket(url) {
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val data = incame.readText()
                // init - игнорим
            }
            send(Frame.Text(request))

            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                response = incame.readText()
            }
        }

        return response
    }
}