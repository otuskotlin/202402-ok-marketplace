package ru.otus.otuskotlin.marketplace.biz.statemachine.general

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.IMkplStateContext

fun <T: IMkplStateContext> ICorChainDsl<T>.initRepo(title: String) = worker {
    this.title = title
    this.description = """
        Вычисление актуального репозитория в зависимости от типа запроса
    """.trimIndent()
}
