package ru.otus.otuskotlin.markeplace.app.spring.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.markeplace.app.spring.base.MkplAppSettings
import ru.otus.otuskotlin.markeplace.app.spring.base.SpringWsSessionRepo
import ru.otus.otuskotlin.marketplace.backend.repo.postgresql.RepoAdSql
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

@Suppress("unused")
@EnableConfigurationProperties(AdConfigPostgres::class)
@Configuration
class AdConfig(val postgresConfig: AdConfigPostgres) {

    val logger: Logger = LoggerFactory.getLogger(AdConfig::class.java)

    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplAdProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoAd = AdRepoInMemory()

    @Bean
    fun prodRepo(): IRepoAd = RepoAdSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

    @Bean
    fun stubRepo(): IRepoAd = AdRepoStub()

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
    )

    @Bean
    fun appSettings(
        corSettings: MkplCorSettings,
        processor: MkplAdProcessor,
    ) = MkplAppSettings(
        corSettings = corSettings,
        processor = processor,
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

}
