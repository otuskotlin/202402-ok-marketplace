package ru.otus.otuskotlin.marketplace.states.common.statemachine

interface ISMAdStateResolver {
    fun resolve(signal: SMAdSignal): SMTransition

    companion object {
        val NONE = object: ISMAdStateResolver {
            override fun resolve(signal: SMAdSignal): SMTransition = SMTransition.ERROR
        }
    }
}
