package ru.otus.otuskotlin.marketplace.biz.statemachine.helper

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.IMkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState

fun <T: IMkplStateContext> ICorChainDsl<T>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == MkplState.NONE }
    handle { state = MkplState.RUNNING }
}
