package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidId
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidLock
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.updateAd(ad: AdUpdateObject, debug: AdDebug = debugStubV1): AdResponseObject =
    updateAd(ad, debug = debug) {
        it should haveSuccessResult
        it.ad shouldNotBe null
        it.ad?.apply {
            if (ad.title != null)
                title shouldBe ad.title
            if (ad.description != null)
                description shouldBe ad.description
            if (ad.adType != null)
                adType shouldBe ad.adType
            if (ad.visibility != null)
                visibility shouldBe ad.visibility
        }
        it.ad!!
    }

suspend fun <T> Client.updateAd(ad: AdUpdateObject, debug: AdDebug = debugStubV1, block: (AdUpdateResponse) -> T): T {
    val id = ad.id
    val lock = ad.lock
    return withClue("updatedV1: $id, lock: $lock, set: $ad") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "ad/update", AdUpdateRequest(
                debug = debug,
                ad = ad.copy(id = id, lock = lock)
            )
        ) as AdUpdateResponse

        response.asClue(block)
    }
}
