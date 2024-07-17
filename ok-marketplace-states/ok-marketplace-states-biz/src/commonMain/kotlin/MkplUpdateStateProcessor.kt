package ru.otus.otuskotlin.marketplace.biz.statemachine

import ru.otus.otuskotlin.marketplace.biz.statemachine.general.computeAdState
import ru.otus.otuskotlin.marketplace.biz.statemachine.general.initRepo
import ru.otus.otuskotlin.marketplace.biz.statemachine.helper.initStatus
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.rootChain
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplAllStatesContext
import ru.otus.otuskotlin.marketplace.states.common.MkplStatesCorSettings

class MkplUpdateStateProcessor(
    private val corSettings: MkplStatesCorSettings = MkplStatesCorSettings.NONE
) {
    suspend fun exec(ctx: MkplAllStatesContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MkplAllStatesContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        readAllStatesWithLock("Чтение и блокировка всех объектов в БД на время вычисления")
        requestStatisticsServer("Запрос сервера статистики")
        computeAdState("Вычисление обновленного состояния")
//        filterUpdated("Вычисляет объявления для сохранения")
        updateStatesWithLock("Чтение и блокировка всех объектов в БД на время вычисления")
    }.build()
}

private fun ICorChainDsl<MkplAllStatesContext>.readAllStatesWithLock(title: String) {
    this.title = title
    this.description = """
        Запрашиваем все объекы из БД
        Проверяем блокировку и время последнего обновления
        - Если блокировка не установлена, ставим свою
        - Если блокировка установлена давно, переписываем на свою
        - Иначе пропускаем, она захвачена другим процессом
        Прочитанные объекты направляем во flow 
    """.trimIndent()
}

private fun ICorChainDsl<MkplAllStatesContext>.requestStatisticsServer(title: String) = worker {
    this.title = title
    this.description = """
        Выполняет батчевые запросы на сервер статистики (в мониторинг, OpenSearch)
    """.trimIndent()
}

private fun ICorChainDsl<MkplAllStatesContext>.updateStatesWithLock(title: String) = worker {
    this.title = title
    this.description = """
        Читает поток вычисленных обновлений и сохраняет их в БД с учетом блокировки.
        Там, где блокировки изменилась, такие объекты пропускаем, их перехватил другой процесс
    """.trimIndent()
}

