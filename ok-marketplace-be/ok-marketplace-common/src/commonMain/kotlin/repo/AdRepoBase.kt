package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.marketplace.common.helpers.errorSystem

abstract class AdRepoBase: IRepoAd {

    protected suspend fun tryAdMethod(block: suspend () -> IDbAdResponse) = try {
        block()
    } catch (e: Throwable) {
        DbAdResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryAdsMethod(block: suspend () -> IDbAdsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbAdsResponseErr(errorSystem("methodException", e = e))
    }

}
