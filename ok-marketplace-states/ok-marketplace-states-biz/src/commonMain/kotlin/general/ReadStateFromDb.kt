package ru.otus.otuskotlin.marketplace.biz.statemachine.general

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext

fun ICorChainDsl<MkplStateContext>.readStateFromDb(title: String) = worker {
    this.title = title
    this.description = """
        Чтение состояния из БД
    """.trimIndent()
}
