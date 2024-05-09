package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidId
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidLock
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.deleteAd(ad: AdResponseObject, debug: AdDebug = debugStubV2) {
    val id = ad.id
    val lock = ad.lock
    withClue("deleteAdV2: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "ad/delete",
            AdDeleteRequest(
                debug = debug,
                ad = AdDeleteObject(id = id, lock = lock)
            )
        ) as AdDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.ad shouldBe ad
//            response.ad?.id shouldBe id
        }
    }
}
