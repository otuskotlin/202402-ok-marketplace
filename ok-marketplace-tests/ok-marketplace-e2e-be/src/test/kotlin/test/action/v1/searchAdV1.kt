package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.searchAd(search: AdSearchFilter, debug: AdDebug = debugStubV1): List<AdResponseObject> = searchAd(search, debug = debug) {
    it should haveSuccessResult
    it.ads ?: listOf()
}

suspend fun <T> Client.searchAd(search: AdSearchFilter, debug: AdDebug = debugStubV1, block: (AdSearchResponse) -> T): T =
    withClue("searchAdV1: $search") {
        val response = sendAndReceive(
            "ad/search",
            AdSearchRequest(
                requestType = "search",
                debug = debug,
                adFilter = search,
            )
        ) as AdSearchResponse

        response.asClue(block)
    }
