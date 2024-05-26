package ru.otus.otuskotlin.marketplace.biz.statemachine.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.helpers.errorValidation
import ru.otus.otuskotlin.marketplace.states.common.helpers.fail
import ru.otus.otuskotlin.marketplace.states.common.models.MkplAdStateId

fun ICorChainDsl<MkplStateContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { rqValidating.adId != MkplAdStateId.NONE && !rqValidating.adId.asString().matches(regExp) }
    handle {
        val encodedId = rqValidating.adId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
