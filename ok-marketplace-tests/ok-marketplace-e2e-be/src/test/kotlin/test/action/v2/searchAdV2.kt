package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.searchAd(search: AdSearchFilter, debug: AdDebug = debugStubV2): List<AdResponseObject> = searchAd(search, debug = debug) {
    it should haveSuccessResult
    it.ads ?: listOf()
}

suspend fun <T> Client.searchAd(search: AdSearchFilter, debug: AdDebug = debugStubV2, block: (AdSearchResponse) -> T): T =
    withClue("searchAdV2: $search") {
        val response: AdSearchResponse = sendAndReceive(
            "ad/search",
            AdSearchRequest(
                debug = debug,
                adFilter = search,
            )
        )

        response.asClue(block)
    }
