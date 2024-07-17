package ru.otus.otuskotlin.marketplace.states.common.statemachine

data class SMTransition(
    val state: SMAdStates,
    val description: String,
) {
    companion object {
        val ERROR = SMTransition(SMAdStates.ERROR, "Unprovided transition occurred")
        val NONE = SMTransition(SMAdStates.NONE, "Empty Transition")
    }
}
