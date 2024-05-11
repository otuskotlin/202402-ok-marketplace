package ru.otus.otuskotlin.markeplace.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.marketplace.api.v2.mappers.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.Test

internal abstract class AdRepoBaseV2Test {
    protected abstract var webClient: WebTestClient
    private val debug = AdDebug(mode = AdRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createAd() = testRepoAd(
        "create",
        AdCreateRequest(
            ad = MkplAdStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(MkplAdStub.prepareResult {
            id = MkplAdId(uuidNew)
            lock = MkplAdLock(uuidNew)
        })
            .toTransportCreate()
    )

    @Test
    open fun readAd() = testRepoAd(
        "read",
        AdReadRequest(
            ad = MkplAdStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(MkplAdStub.get())
            .toTransportRead()
    )

    @Test
    open fun updateAd() = testRepoAd(
        "update",
        AdUpdateRequest(
            ad = MkplAdStub.prepareResult { title = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(MkplAdStub.prepareResult {
            title = "add"
            lock = MkplAdLock(uuidNew)
        })
            .toTransportUpdate()
    )

    @Test
    open fun deleteAd() = testRepoAd(
        "delete",
        AdDeleteRequest(
            ad = MkplAdStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(MkplAdStub.get())
            .toTransportDelete()
    )

    @Test
    open fun searchAd() = testRepoAd(
        "search",
        AdSearchRequest(
            adFilter = AdSearchFilter(adType = DealSide.SUPPLY),
            debug = debug,
        ),
        MkplContext(
            state = MkplState.RUNNING,
            adsResponse = MkplAdStub.prepareSearchList("xx", MkplDealSide.SUPPLY)
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch()
    )

    @Test
    open fun offersAd() = testRepoAd(
        "offers",
        AdOffersRequest(
            ad = MkplAdStub.get().toTransportRead(),
            debug = debug,
        ),
        MkplContext(
            state = MkplState.RUNNING,
            adResponse = MkplAdStub.prepareResult { permissionsClient.clear() },
            adsResponse = MkplAdStub.prepareSearchList("xx", MkplDealSide.SUPPLY)
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportOffers()
    )

    private fun prepareCtx(ad: MkplAd) = MkplContext(
        state = MkplState.RUNNING,
        adResponse = ad.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoAd(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v2/ad/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is AdSearchResponse -> it.copy(ads = it.ads?.sortedBy { it.id })
                    is AdOffersResponse -> it.copy(ads = it.ads?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
