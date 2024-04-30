package ru.otus.otuskotlin.marketplace.backend.repository.inmemory

import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub

class AdRepoStub() : IRepoAd {
    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse {
        return DbAdsResponseOk(
            data = MkplAdStub.prepareSearchList(filter = "", MkplDealSide.DEMAND),
        )
    }
}
