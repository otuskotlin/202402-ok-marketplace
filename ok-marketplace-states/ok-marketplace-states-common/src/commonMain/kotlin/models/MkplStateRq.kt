package ru.otus.otuskotlin.marketplace.states.common.models

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.states.common.NONE
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMAdStates
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMTransition

data class MkplStateRq(
    var adId: MkplAdStateId = MkplAdStateId.NONE,
    var oldState: SMAdStates = SMAdStates.NONE,
    var created: Instant = Instant.NONE,
    var views: Int = 0,
    var transition: SMTransition = SMTransition.ERROR,
) {
    fun deepCopy() = copy()
}
