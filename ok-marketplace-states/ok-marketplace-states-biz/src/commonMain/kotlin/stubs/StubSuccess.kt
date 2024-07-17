package ru.otus.otuskotlin.marketplace.biz.statemachine.stubs

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.models.MkplAdStateId
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState
import ru.otus.otuskotlin.marketplace.states.common.models.MkplStateRq
import ru.otus.otuskotlin.marketplace.states.common.models.MkplStubs
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMAdStates
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMTransition
import kotlin.time.Duration.Companion.days

fun ICorChainDsl<MkplStateContext>.stubSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Обрабатываем сценарий стаба с успешным запросом, когда возвращается объект с состоянием
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    handle {
        stateResponse = MkplStateRq(
            adId = stateRequest.adId.takeIf { it != MkplAdStateId.NONE } ?: MkplAdStateId("123"),
            oldState = SMAdStates.ACTUAL,
            created = Clock.System.now() - 3.days,
            views = 10,
            transition = SMTransition.NONE
        )
        state = MkplState.FINISHING
    }
}
