package ru.otus.otuskotlin.marketplace.logging.socket

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
