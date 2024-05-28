package ru.otus.otuskotlin.marketplace.app.ktor.base

import io.ktor.websocket.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSession

data class KtorWsSessionV2(
    private val session: WebSocketSession
) : IMkplWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV2ResponseSerialize(obj)))
    }
}
