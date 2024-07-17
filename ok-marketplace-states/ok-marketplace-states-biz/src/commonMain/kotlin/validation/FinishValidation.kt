package ru.otus.otuskotlin.marketplace.biz.statemachine.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState

fun ICorChainDsl<MkplStateContext>.finishValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        rqValidated = rqValidating
    }
}
