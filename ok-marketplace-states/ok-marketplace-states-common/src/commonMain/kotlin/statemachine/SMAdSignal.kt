package ru.otus.otuskotlin.marketplace.states.common.statemachine

import kotlin.time.Duration

data class SMAdSignal(
    val state: SMAdStates,
    val duration: Duration,
    val views: Int,
)
