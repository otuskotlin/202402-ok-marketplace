package ru.otus.otuskotlin.marketplace.backend.repo.tests

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.repo.*

class AdRepositoryMock(
    private val invokeCreateAd: (DbAdRequest) -> IDbAdResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadAd: (DbAdIdRequest) -> IDbAdResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateAd: (DbAdRequest) -> IDbAdResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteAd: (DbAdIdRequest) -> IDbAdResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchAd: (DbAdFilterRequest) -> IDbAdsResponse = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepoAd {
    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse {
        return invokeCreateAd(rq)
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse {
        return invokeReadAd(rq)
    }

    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse {
        return invokeUpdateAd(rq)
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse {
        return invokeDeleteAd(rq)
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse {
        return invokeSearchAd(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbAdResponseOk(MkplAd())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbAdsResponseOk(emptyList())
    }
}
