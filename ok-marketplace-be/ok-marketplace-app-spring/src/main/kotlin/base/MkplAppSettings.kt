package ru.otus.otuskotlin.markeplace.app.spring.base

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

data class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplAdProcessor,
): IMkplAppSettings
