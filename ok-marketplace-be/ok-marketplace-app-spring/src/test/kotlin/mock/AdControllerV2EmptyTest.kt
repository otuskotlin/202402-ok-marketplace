package ru.otus.otuskotlin.markeplace.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.markeplace.app.spring.config.AdConfig
import ru.otus.otuskotlin.markeplace.app.spring.controllers.AdControllerV2Fine
import ru.otus.otuskotlin.marketplace.api.v2.mappers.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(AdControllerV2Fine::class, AdConfig::class)
internal class AdControllerV2EmptyTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: MkplAdProcessor

    @Test
    fun createAd() = testStubAd(
        "/v2/ad/create",
        AdCreateRequest(),
        MkplContext().toTransportCreate()
    )

    @Test
    fun readAd() = testStubAd(
        "/v2/ad/read",
        AdReadRequest(),
        MkplContext().toTransportRead()
    )

    @Test
    fun updateAd() = testStubAd(
        "/v2/ad/update",
        AdUpdateRequest(),
        MkplContext().toTransportUpdate()
    )

    @Test
    fun deleteAd() = testStubAd(
        "/v2/ad/delete",
        AdDeleteRequest(),
        MkplContext().toTransportDelete()
    )

    @Test
    fun searchAd() = testStubAd(
        "/v2/ad/search",
        AdSearchRequest(),
        MkplContext().toTransportSearch()
    )

    @Test
    fun searchOffers() = testStubAd(
        "/v2/ad/offers",
        AdOffersRequest(),
        MkplContext().toTransportOffers()
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubAd(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
