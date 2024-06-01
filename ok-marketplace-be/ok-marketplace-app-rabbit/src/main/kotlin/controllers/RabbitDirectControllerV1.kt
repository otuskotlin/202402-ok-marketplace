package ru.otus.otuskotlin.marketplace.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectControllerV1(
    private val appSettings: MkplAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV1,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportAd()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectControllerV1::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = MkplContext()
        e.printStackTrace()
        context.state = MkplState.FAILING
        context.errors.add(e.asMkplError())
        val response = context.toTransportAd()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
