package ru.otus.otuskotlin.markeplace.app.spring.base

import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSession

data class SpringWsSessionV2(
    private val session: WebSocketSession,
) : IMkplWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        val message = apiV2ResponseSerialize(obj)
        println("SENDING to WsV1: $message")
        session.send(Mono.just(session.textMessage(message)))
    }
}
