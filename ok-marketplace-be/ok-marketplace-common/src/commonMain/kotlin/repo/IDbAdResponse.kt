package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplError

sealed interface IDbAdResponse: IDbResponse<MkplAd>

data class DbAdResponseOk(
    val data: MkplAd
): IDbAdResponse

data class DbAdResponseErr(
    val errors: List<MkplError> = emptyList()
): IDbAdResponse {
    constructor(err: MkplError): this(listOf(err))
}

data class DbAdResponseErrWithData(
    val data: MkplAd,
    val errors: List<MkplError> = emptyList()
): IDbAdResponse {
    constructor(ad: MkplAd, err: MkplError): this(ad, listOf(err))
}
