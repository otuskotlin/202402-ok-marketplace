package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MkplCommand
    private val settings by lazy { MkplCorSettings() }
    protected val processor by lazy { MkplAdProcessor(settings) }
}
