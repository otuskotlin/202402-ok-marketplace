package ru.otus.otuskotlin.marketplace.states.common

import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.states.common.statemachine.ISMAdStateResolver

data class MkplStatesCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val stateMachine: ISMAdStateResolver = ISMAdStateResolver.NONE
) {
    companion object {
        val NONE = MkplStatesCorSettings()
    }
}

