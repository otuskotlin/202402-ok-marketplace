package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

data class MkplAppSettings(
    override val corSettings: MkplCorSettings = MkplCorSettings(),
    override val processor: MkplAdProcessor = MkplAdProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
    override val controllersConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IMkplAppSettings, IMkplAppRabbitSettings
