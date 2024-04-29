package ru.otus.otuskotlin.marketplace.biz.repo

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.fail
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseErr
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseErrWithData
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponseOk
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker

fun ICorChainDsl<MkplContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdIdRequest(adRepoPrepare)
        when(val result = adRepo.deleteAd(request)) {
            is DbAdResponseOk -> adRepoDone = result.data
            is DbAdResponseErr -> {
                fail(result.errors)
                adRepoDone = adRepoRead
            }
            is DbAdResponseErrWithData -> {
                fail(result.errors)
                adRepoDone = result.data
            }
        }
    }
}
