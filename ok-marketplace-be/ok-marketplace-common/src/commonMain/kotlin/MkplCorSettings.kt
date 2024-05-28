package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSessionRepo
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val wsSessions: IMkplWsSessionRepo = IMkplWsSessionRepo.NONE,
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
