package ru.otus.otuskotlin.marketplace.common.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.marketplace.common.helpers.errorSystem
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class AdRepoBase: IRepoAd {

    protected suspend fun tryAdMethod(timeout: Duration = 10.seconds, ctx: CoroutineContext = Dispatchers.IO, block: suspend () -> IDbAdResponse) = try {
        withTimeout(timeout) {
            withContext(ctx) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbAdResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryAdsMethod(block: suspend () -> IDbAdsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbAdsResponseErr(errorSystem("methodException", e = e))
    }

}
