package ru.otus.otuskotlin.marketplace.biz.repo

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseErr
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseErrWithData
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<MkplContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdRequest(adRepoPrepare)
        when(val result = adRepo.createAd(request)) {
            is DbAdResponseOk -> adRepoDone = result.data
            is DbAdResponseErr -> fail(result.errors)
            is DbAdResponseErrWithData -> fail(result.errors)
        }
    }
}
