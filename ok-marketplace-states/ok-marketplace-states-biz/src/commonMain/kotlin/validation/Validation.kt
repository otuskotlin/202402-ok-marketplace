package ru.otus.otuskotlin.marketplace.biz.statemachine.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState

fun ICorChainDsl<MkplStateContext>.validation(block: ICorChainDsl<MkplStateContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MkplState.RUNNING }
}
