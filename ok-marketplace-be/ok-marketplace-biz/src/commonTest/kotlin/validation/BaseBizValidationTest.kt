package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub

abstract class BaseBizValidationTest {
    protected abstract val command: MkplCommand
    private val repo = AdRepoInitialized(
        repo = AdRepoInMemory(),
        initObjects = listOf(
            MkplAdStub.get(),
        ),
    )
    private val settings by lazy { MkplCorSettings(repoTest = repo) }
    protected val processor by lazy { MkplAdProcessor(settings) }
}
