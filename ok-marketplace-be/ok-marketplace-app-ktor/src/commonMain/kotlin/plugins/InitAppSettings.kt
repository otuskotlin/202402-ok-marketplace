package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionRepo
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.biz.statemachine.resolver.SMAdStateResolverDefault
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.states.common.MkplStatesCorSettings

fun Application.initAppSettings(): MkplAppSettings {
    val loggerProvider = getLoggerProviderConf()
    val corSettings = MkplCorSettings(
        loggerProvider = loggerProvider,
        wsSessions = KtorWsSessionRepo(),
        repoTest = getDatabaseConf(AdDbType.TEST),
        repoProd = getDatabaseConf(AdDbType.PROD),
        repoStub = AdRepoStub(),
        stateSettings = MkplStatesCorSettings(
            loggerProvider = loggerProvider,
            stateMachine = SMAdStateResolverDefault(),
        ),
    )
    return MkplAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MkplAdProcessor(corSettings),
    )
}
