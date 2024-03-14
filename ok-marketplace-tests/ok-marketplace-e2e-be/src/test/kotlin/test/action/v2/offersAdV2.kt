package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.offersAd(id: String?): AdOffersResponse = offersAd(id) {
    it should haveSuccessResult
    it
}

suspend fun <T> Client.offersAd(id: String?, block: (AdOffersResponse) -> T): T =
    withClue("searchOffersV2: $id") {
        val response = sendAndReceive(
            "ad/offers",
            AdOffersRequest(
                debug = debug,
                ad = AdReadObject(id = id),
            )
        ) as AdOffersResponse

        response.asClue(block)
    }
