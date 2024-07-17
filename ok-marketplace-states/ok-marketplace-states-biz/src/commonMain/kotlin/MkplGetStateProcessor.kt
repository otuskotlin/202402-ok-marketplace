package ru.otus.otuskotlin.marketplace.biz.statemachine

import ru.otus.otuskotlin.marketplace.biz.statemachine.general.initRepo
import ru.otus.otuskotlin.marketplace.biz.statemachine.general.prepareResponse
import ru.otus.otuskotlin.marketplace.biz.statemachine.general.readStateFromDb
import ru.otus.otuskotlin.marketplace.biz.statemachine.helper.initStatus
import ru.otus.otuskotlin.marketplace.biz.statemachine.stubs.stubNoCase
import ru.otus.otuskotlin.marketplace.biz.statemachine.stubs.stubSuccess
import ru.otus.otuskotlin.marketplace.biz.statemachine.stubs.stubs
import ru.otus.otuskotlin.marketplace.biz.statemachine.validation.finishValidation
import ru.otus.otuskotlin.marketplace.biz.statemachine.validation.validateIdNotEmpty
import ru.otus.otuskotlin.marketplace.biz.statemachine.validation.validateIdProperFormat
import ru.otus.otuskotlin.marketplace.biz.statemachine.validation.validation
import ru.otus.otuskotlin.marketplace.cor.rootChain
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.MkplStatesCorSettings
import ru.otus.otuskotlin.marketplace.states.common.models.MkplAdStateId

class MkplAdStateProcessor(
    private val corSettings: MkplStatesCorSettings = MkplStatesCorSettings.NONE
) {
    suspend fun exec(ctx: MkplStateContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MkplStateContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        stubs("Обработка стабов") {
            stubSuccess("Успешный сценарий")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
        validation {
            worker("Копируем поля в adValidating") { rqValidating = stateRequest.deepCopy() }
            worker("Очистка id") { rqValidating.adId = MkplAdStateId(rqValidating.adId.asString().trim()) }
            validateIdNotEmpty("Проверка на непустой id")
            validateIdProperFormat("Проверка формата id")

            finishValidation("Успешное завершение процедуры валидации")
        }
        readStateFromDb("Чтение состояния из БД")
        prepareResponse("Подготовка ответа")
    }.build()
}
