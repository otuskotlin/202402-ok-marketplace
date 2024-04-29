package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplError

sealed interface IDbAdsResponse: IDbResponse<List<MkplAd>>

data class DbAdsResponseOk(
    val data: List<MkplAd>
): IDbAdsResponse

@Suppress("unused")
data class DbAdsResponseErr(
    val errors: List<MkplError> = emptyList()
): IDbAdsResponse {
    constructor(err: MkplError): this(listOf(err))
}
