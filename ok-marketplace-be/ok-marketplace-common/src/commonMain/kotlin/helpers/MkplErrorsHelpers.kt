package ru.otus.otuskotlin.marketplace.common.helpers

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
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

fun MkplContext.addError(error: MkplError) = errors.add(error)
fun MkplContext.addErrors(error: Collection<MkplError>) = errors.addAll(error)

fun MkplContext.fail(error: MkplError) {
    addError(error)
    state = MkplState.FAILING
}

fun MkplContext.fail(errors: Collection<MkplError>) {
    addErrors(errors)
    state = MkplState.FAILING
}

fun errorValidation(
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

fun errorSystem(
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

fun accessViolation(
    principal: MkplPrincipalModel,
    operation: MkplCommand,
    adId: MkplAdId = MkplAdId.NONE,
) = MkplError(
    code = "access-${operation.name.lowercase()}",
    group = "access",
    message = "User ${principal.genericName()} (${principal.id.asString()}) is not allowed to perform operation ${operation.name}"
            + if (adId != MkplAdId.NONE) " on ad ${adId.asString()}" else "",
    level = LogLevel.ERROR,
)
