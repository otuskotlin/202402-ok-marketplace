package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client

suspend fun Client.createAd(ad: AdCreateObject = someCreateAd): AdResponseObject = createAd(ad) {
    it should haveSuccessResult
    it.ad shouldNotBe null
    it.ad?.apply {
        title shouldBe ad.title
        description shouldBe ad.description
        adType shouldBe ad.adType
        visibility shouldBe ad.visibility
    }
    it.ad!!
}

suspend fun <T> Client.createAd(ad: AdCreateObject = someCreateAd, block: (AdCreateResponse) -> T): T =
    withClue("createAdV2: $ad") {
        val response = sendAndReceive(
            "ad/create", AdCreateRequest(
                debug = debug,
                ad = ad
            )
        ) as AdCreateResponse

        response.asClue(block)
    }
