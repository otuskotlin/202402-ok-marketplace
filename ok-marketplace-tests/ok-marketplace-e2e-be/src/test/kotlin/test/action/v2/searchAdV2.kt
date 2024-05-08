package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.marketplace.api.v2.models.AdResponseObject
import ru.otus.otuskotlin.marketplace.api.v2.models.AdSearchFilter
import ru.otus.otuskotlin.marketplace.api.v2.models.AdSearchRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdSearchResponse
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.searchAd(search: AdSearchFilter): List<AdResponseObject> = searchAd(search) {
    it should haveSuccessResult
    it.ads ?: listOf()
}

suspend fun <T> Client.searchAd(search: AdSearchFilter, block: (AdSearchResponse) -> T): T =
    withClue("searchAdV2: $search") {
        val response = sendAndReceive(
            "ad/search",
            AdSearchRequest(
                debug = debug,
                adFilter = search,
            )
        ) as AdSearchResponse

        response.asClue(block)
    }
