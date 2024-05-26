package ru.otus.otuskotlin.marketplace.states.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.states.common.models.*

data class MkplAllStatesContext(
    override var state: MkplState = MkplState.NONE,
    override val errors: MutableList<MkplError> = mutableListOf(),

    override var corSettings: MkplStatesCorSettings = MkplStatesCorSettings(),

    override var timeStart: Instant = Instant.NONE,

    var statesRead: Flow<MkplStateRq> = flowOf(),
    var statesWStat: Flow<MkplStateRq> = flowOf(),
    var statesComputed: Flow<MkplStateRq> = flowOf(),
    var statesUpdating: Flow<MkplStateRq> = flowOf(),
): IMkplStateContext
