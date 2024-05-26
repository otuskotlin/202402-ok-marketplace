package ru.otus.otuskotlin.marketplace.biz.general

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<MkplContext>.getAdStates(title: String) = worker {
    this.title = title
    this.description = """
        Получаем состояние из сервиса состояний
    """.trimIndent()
    on { state == MkplState.RUNNING }
    handle {
        corSettings.stateSettings.stateMachine
    }
}
