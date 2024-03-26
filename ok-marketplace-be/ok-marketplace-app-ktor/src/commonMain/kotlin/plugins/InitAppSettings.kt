package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

fun Application.initAppSettings(): MkplAppSettings {
    val corSettings = MkplCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return MkplAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MkplAdProcessor(corSettings),
    )
}
