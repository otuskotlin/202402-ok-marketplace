package ru.otus.otuskotlin.marketplace.biz.statemachine.stubs

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState
import ru.otus.otuskotlin.marketplace.states.common.models.MkplWorkMode

fun ICorChainDsl<MkplStateContext>.stubs(title: String, block: ICorChainDsl<MkplStateContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MkplWorkMode.STUB && state == MkplState.RUNNING }
}
