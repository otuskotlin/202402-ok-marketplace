package ru.otus.otuskotlin.marketplace.biz.repo

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponseErr
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponseOk
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<MkplContext>.repoOffers(title: String) = worker {
    this.title = title
    description = "Поиск предложений для объявления по названию"
    on { state == MkplState.RUNNING }
    handle {
        val adRequest = adRepoPrepare
        val filter = DbAdFilterRequest(
            // Здесь должен быть более умный поиск. Такой примитив слишком плохо работает
            // titleFilter = adRequest.title,
            dealSide = when (adRequest.adType) {
                MkplDealSide.DEMAND -> MkplDealSide.SUPPLY
                MkplDealSide.SUPPLY -> MkplDealSide.DEMAND
                MkplDealSide.NONE -> {
                    fail(
                        MkplError(
                            field = "adType",
                            message = "Type of ad must not be empty"
                        )
                    )
                    return@handle
                }
            },
        )

        when (val dbResponse = adRepo.searchAd(filter)) {
            is DbAdsResponseOk -> adsRepoDone = dbResponse.data.toMutableList()
            is DbAdsResponseErr -> fail(dbResponse.errors)
        }
    }
}
