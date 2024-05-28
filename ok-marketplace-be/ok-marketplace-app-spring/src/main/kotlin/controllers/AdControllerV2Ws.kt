package ru.otus.otuskotlin.markeplace.app.spring.controllers

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.otuskotlin.markeplace.app.spring.base.MkplAppSettings
import ru.otus.otuskotlin.markeplace.app.spring.base.SpringWsSessionV2
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportAd
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand

@Component
class AdControllerV2Ws(private val appSettings: MkplAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> = runBlocking {
        val mkplSess = SpringWsSessionV2(session)
        sessions.add(mkplSess)
        val messageObj = process("ws-v2-init") {
            command = MkplCommand.INIT
            wsSession = mkplSess
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v2-handle") {
                    wsSession = mkplSess
                    val request = apiV2RequestDeserialize<IRequest>(message.payloadAsText)
                    fromTransport(request)
                }
            }

        val output = merge(flowOf(messageObj), messages)
            .onCompletion {
                process("ws-v2-finish") {
                    command = MkplCommand.FINISH
                    wsSession = mkplSess
                }
                sessions.remove(mkplSess)
            }
            .map { session.textMessage(apiV2ResponseSerialize(it)) }
            .asFlux()
        session.send(output)
    }

    private suspend fun process(logId: String, function: MkplContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = MkplContext::toTransportAd,
        clazz = this@AdControllerV2Ws::class,
        logId = logId,
    )
}
