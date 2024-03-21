package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

interface IMkplAppSettings {
    val processor: MkplAdProcessor
    val corSettings: MkplCorSettings
}
