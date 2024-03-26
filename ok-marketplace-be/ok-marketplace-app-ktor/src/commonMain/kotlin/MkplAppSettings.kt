package ru.otus.otuskotlin.marketplace.app.ktor

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkplCorSettings = MkplCorSettings(),
    override val processor: MkplAdProcessor = MkplAdProcessor(corSettings),
): IMkplAppSettings
