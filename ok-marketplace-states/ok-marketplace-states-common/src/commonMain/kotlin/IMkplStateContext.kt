package ru.otus.otuskotlin.marketplace.states.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.states.common.models.MkplError
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState

interface IMkplStateContext {
    var state: MkplState
    val errors: MutableList<MkplError>

    var corSettings: MkplStatesCorSettings

    var timeStart: Instant

}
