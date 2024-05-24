package ru.otus.otuskotlin.markeplace.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback

@Suppress("unused")
@Configuration
class AdConfig {
    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplAdProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: MkplCorSettings,
        processor: MkplAdProcessor,
    ) = MkplAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
