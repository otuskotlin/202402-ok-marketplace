package ru.otus.otuskotlin.marketplace.states.common.helpers

import ru.otus.otuskotlin.marketplace.logging.common.LogLevel
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.models.MkplError
import ru.otus.otuskotlin.marketplace.states.common.models.MkplState

inline fun MkplStateContext.addError(error: MkplError) = errors.add(error)
inline fun MkplStateContext.addErrors(error: Collection<MkplError>) = errors.addAll(error)

inline fun MkplStateContext.fail(error: MkplError) {
    addError(error)
    state = MkplState.FAILING
}

inline fun MkplStateContext.fail(errors: Collection<MkplError>) {
    addErrors(errors)
    state = MkplState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MkplError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = MkplError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
