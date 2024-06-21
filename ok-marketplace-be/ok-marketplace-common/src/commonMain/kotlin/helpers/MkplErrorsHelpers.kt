package ru.otus.otuskotlin.marketplace.common.helpers

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel

fun Throwable.asMkplError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MkplError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun MkplContext.addError(error: MkplError) = errors.add(error)
inline fun MkplContext.addErrors(error: Collection<MkplError>) = errors.addAll(error)

inline fun MkplContext.fail(error: MkplError) {
    addError(error)
    state = MkplState.FAILING
}

inline fun MkplContext.fail(errors: Collection<MkplError>) {
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
