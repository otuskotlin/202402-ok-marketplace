package ru.otus.otuskotlin.marketplace.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportAd
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState

class RabbitDirectControllerV2(
    private val appSettings: MkplAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV2,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {

    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV2RequestDeserialize<IRequest>(String(message.body))
                fromTransport(req)
            },
            {
                val res = toTransportAd()
                apiV2ResponseSerialize(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
                }
            },
            RabbitDirectControllerV2::class,
            "rabbitmq-v2-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = MkplContext()
        e.printStackTrace()
        context.state = MkplState.FAILING
        context.errors.add(e.asMkplError())
        val response = context.toTransportAd()
        apiV2ResponseSerialize(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
        }
    }
}
