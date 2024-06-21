package ru.otus.otuskotlin.marketplace.biz.repo

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponseErr
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponseOk
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<MkplContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdFilterRequest(
            titleFilter = adFilterValidated.searchString,
            ownerId = adFilterValidated.ownerId,
            dealSide = adFilterValidated.dealSide,
        )
        when(val result = adRepo.searchAd(request)) {
            is DbAdsResponseOk -> adsRepoDone = result.data.toMutableList()
            is DbAdsResponseErr -> fail(result.errors)
        }
    }
}
