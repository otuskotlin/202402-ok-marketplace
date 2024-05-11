package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.marketplace.api.v2.models.AdDebug
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.api.v2.models.AdReadObject
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.offersAd(id: String?, debug: AdDebug = debugStubV2): AdOffersResponse = offersAd(id, debug = debug) {
    it should haveSuccessResult
    it
}

suspend fun <T> Client.offersAd(id: String?, debug: AdDebug = debugStubV2, block: (AdOffersResponse) -> T): T =
    withClue("searchOffersV2: $id") {
        val response: AdOffersResponse = sendAndReceive(
            "ad/offers",
            AdOffersRequest(
                debug = debug,
                ad = AdReadObject(id = id),
            )
        )

        response.asClue(block)
    }
