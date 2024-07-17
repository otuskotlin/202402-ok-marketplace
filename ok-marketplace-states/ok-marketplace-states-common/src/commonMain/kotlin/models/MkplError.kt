package ru.otus.otuskotlin.marketplace.states.common.models

import ru.otus.otuskotlin.marketplace.logging.common.LogLevel

data class MkplError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
