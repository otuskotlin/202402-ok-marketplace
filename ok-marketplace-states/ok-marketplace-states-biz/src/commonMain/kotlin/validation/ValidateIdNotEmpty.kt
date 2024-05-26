package ru.otus.otuskotlin.marketplace.biz.statemachine.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.states.common.helpers.fail

fun ICorChainDsl<MkplStateContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { rqValidating.adId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
