package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings

interface IMkplAppRabbitSettings: IMkplAppSettings {
    val rabbit: RabbitConfig
    val controllersConfigV1: RabbitExchangeConfiguration
    val controllersConfigV2: RabbitExchangeConfiguration
}
