package ru.otus.otuskotlin.marketplace.biz.statemachine.stubs

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.helpers.fail
import ru.otus.otuskotlin.marketplace.states.common.models.MkplError
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState

fun ICorChainDsl<MkplStateContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == MkplState.RUNNING }
    handle {
        fail(
            MkplError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
