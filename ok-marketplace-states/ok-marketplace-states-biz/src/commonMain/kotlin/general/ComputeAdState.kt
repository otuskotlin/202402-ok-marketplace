package ru.otus.otuskotlin.marketplace.biz.statemachine.general

import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplAllStatesContext
import ru.otus.otuskotlin.marketplace.states.common.NONE
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState
import ru.otus.otuskotlin.marketplace.states.common.models.MkplStateRq
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMAdSignal
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMAdStates
import kotlin.reflect.KClass

private val clazz: KClass<*> = ICorChainDsl<MkplAllStatesContext>::computeAdState::class
fun ICorChainDsl<MkplAllStatesContext>.computeAdState(title: String) = worker {
    this.title = title
    this.description = "Вычисление состояния объявления"
    on { state == MkplState.RUNNING }
    handle {
        val machine = this.corSettings.stateMachine
        val log = corSettings.loggerProvider.logger(clazz)
        statesComputed = statesWStat.onEach { sc: MkplStateRq ->
            val timeNow = Clock.System.now()
            val timePublished = sc.created.takeIf { it != Instant.NONE } ?: timeNow
            val signal = SMAdSignal(
                state = sc.oldState.takeIf { it != SMAdStates.NONE } ?: SMAdStates.NEW,
                duration = timeNow - timePublished,
                views = sc.views,
            )
            val transition = machine.resolve(signal)
            if (transition.state != sc.oldState) {
                log.info("New ad state transition: ${transition.description}")
            }
            sc.transition = transition
        }
    }
}
