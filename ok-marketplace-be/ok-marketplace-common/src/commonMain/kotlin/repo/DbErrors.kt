package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplError

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MkplAdId) = DbAdResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbAdResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)
