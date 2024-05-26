package ru.otus.otuskotlin.marketplace.states.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.states.common.models.*

data class MkplStateContext(
    override var state: MkplState = MkplState.NONE,
    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,
    override val errors: MutableList<MkplError> = mutableListOf(),

    override var corSettings: MkplStatesCorSettings = MkplStatesCorSettings(),

    override var timeStart: Instant = Instant.NONE,

    var stateRequest: MkplStateRq = MkplStateRq(),
    var rqValidating: MkplStateRq = MkplStateRq(),
    var rqValidated: MkplStateRq = MkplStateRq(),

    var stateRead: MkplStateRq = MkplStateRq(),
    var stateWStats: MkplStateRq = MkplStateRq(),

    var stateResponse: MkplStateRq = MkplStateRq(),
): IMkplStateContext
