package ru.otus.otuskotlin.markeplace.app.spring.mock

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference

abstract class AdControllerBaseWsTest<Rq, Rs>(
    private val version: String
) {

    abstract var port: Int

    abstract fun serializeRq(request: Rq): String
    abstract fun deserializeRs(response: String): Rs

    protected fun testWsApp(request: Rq, assertion: (List<Rs>) -> Unit) = runBlocking {
        val client: WebSocketClient = ReactorNettyWebSocketClient()
        val uri = URI.create("ws://localhost:$port/$version/ws")
        val actualRef = AtomicReference<List<String>>()

        client.execute(uri) { webSocketSession: WebSocketSession ->
            webSocketSession
                .send(
                    Mono.just(
                        webSocketSession.textMessage(serializeRq(request))
                    )
                )
                .thenMany(webSocketSession.receive().take(2).map(WebSocketMessage::getPayloadAsText))
                .collectList()
                .doOnNext(actualRef::set)
                .then()
        }.block(Duration.ofSeconds(5))

        assertThat(actualRef.get()).isNotNull()

        val payload = actualRef.get().map { deserializeRs(it) }
        assertion(payload)
    }
}
