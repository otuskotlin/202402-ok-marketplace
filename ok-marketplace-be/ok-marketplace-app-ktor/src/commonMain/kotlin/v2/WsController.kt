package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportAd
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportInit
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionV2
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import kotlin.reflect.KClass

private val clWsV2: KClass<*> = WebSocketSession::wsHandlerV2::class
suspend fun WebSocketSession.wsHandlerV2(appSettings: MkplAppSettings) = with(KtorWsSessionV2(this)) {
    // Обновление реестра сессий
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = MkplCommand.INIT
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV2ResponseSerialize(toTransportInit()))) },
        clWsV2,
        "wsV2-init"
    )

    // Handle flow
    incoming.receiveAsFlow()
        .mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull
            // Handle without flow destruction
            try {
                appSettings.controllerHelper(
                    {
                        fromTransport(apiV2RequestDeserialize<IRequest>(frame.readText()))
                        wsSession = this@with
                    },
                    {
                        val result = apiV2ResponseSerialize(toTransportAd())
                        // If change request, response is sent to everyone
                        outgoing.send(Frame.Text(result))
                    },
                    clWsV2,
                    "wsV2-handle"
                )

            } catch (_: ClosedReceiveChannelException) {
                sessions.remove(this@with)
            } catch (e: Throwable) {
                println("FFF")
            }
        }
        .onCompletion {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = MkplCommand.FINISH
                    wsSession = this@with
                },
                { },
                clWsV2,
                "wsV2-finish"
            )
            sessions.remove(this@with)
        }
        .collect()
}
