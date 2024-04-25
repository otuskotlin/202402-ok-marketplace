package ru.otus.otuskotlin.marketplace.api.v2.mappers

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs

// Демонстрация форматной валидации в мапере
private sealed interface Result<T,E>
private data class Ok<T,E>(val value: T) : Result<T,E>
private data class Err<T,E>(val errors: List<E>) : Result<T,E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T,E> Result<T,E>.getOrExec(default: T, block: (Err<T,E>) -> Unit = {}): T = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T,E> Result<T,E>.getOrNull(block: (Err<T,E>) -> Unit = {}): T? = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<MkplStubs,MkplError> = when (this) {
    "success" -> Ok(MkplStubs.SUCCESS)
    "notFound" -> Ok(MkplStubs.NOT_FOUND)
    "badId" -> Ok(MkplStubs.BAD_ID)
    "badTitle" -> Ok(MkplStubs.BAD_TITLE)
    "badDescription" -> Ok(MkplStubs.BAD_DESCRIPTION)
    "badVisibility" -> Ok(MkplStubs.BAD_VISIBILITY)
    "cannotDelete" -> Ok(MkplStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(MkplStubs.BAD_SEARCH_STRING)
    null -> Ok(MkplStubs.NONE)
    else -> Err(
        MkplError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun MkplContext.fromTransportValidated(request: AdCreateRequest) {
    command = MkplCommand.CREATE
    // Вся магия здесь!
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(MkplStubs.NONE) { err: Err<MkplStubs,MkplError> ->
            errors.addAll(err.errors)
            state = MkplState.FAILING
        }
}
