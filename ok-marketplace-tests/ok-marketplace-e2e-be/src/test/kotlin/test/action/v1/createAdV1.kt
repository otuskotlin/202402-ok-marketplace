package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.createAd(ad: AdCreateObject = someCreateAd, debug: AdDebug = debugStubV1): AdResponseObject = createAd(ad, debug = debug) {
    it should haveSuccessResult
    it.ad shouldNotBe null
    it.ad?.apply {
        title shouldBe ad.title
        description shouldBe ad.description
        adType shouldBe ad.adType
        visibility shouldBe ad.visibility
        id.toString() shouldMatch Regex("^[\\d\\w_-]+\$")
        lock.toString() shouldMatch Regex("^[\\d\\w_-]+\$")
    }
    it.ad!!
}

suspend fun <T> Client.createAd(ad: AdCreateObject = someCreateAd, debug: AdDebug = debugStubV1, block: (AdCreateResponse) -> T): T =
    withClue("createAdV1: $ad") {
        val response = sendAndReceive(
            "ad/create", AdCreateRequest(
                requestType = "create",
                debug = debug,
                ad = ad
            )
        ) as AdCreateResponse

        response.asClue(block)
    }
