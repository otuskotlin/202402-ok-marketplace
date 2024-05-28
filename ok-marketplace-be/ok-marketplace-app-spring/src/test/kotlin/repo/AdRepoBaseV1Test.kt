package ru.otus.otuskotlin.markeplace.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.common.AUTH_HEADER
import ru.otus.otuskotlin.marketplace.app.common.createJwtTestHeader
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups
import ru.otus.otuskotlin.marketplace.mappers.v1.*
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubBolts
import kotlin.test.Test

internal abstract class AdRepoBaseV1Test {
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
            .copy(responseType = "create")
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
            .copy(responseType = "read")
    )

    @Test
    open fun updateAd() = testRepoAd(
        "update",
        AdUpdateRequest(
            ad = MkplAdStub.prepareResult { title = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(MkplAdStub.prepareResult { title = "add"; lock = MkplAdLock(uuidNew) })
            .toTransportUpdate().copy(responseType = "update")
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
            .copy(responseType = "delete")
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
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
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
            adResponse = MkplAdStub.get(),
            adsResponse = MkplAdStub.prepareSearchList("xx", MkplDealSide.SUPPLY)
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportOffers().copy(responseType = "offers")
    )

    private fun prepareCtx(ad: MkplAd) = MkplContext(
        state = MkplState.RUNNING,
        adResponse = ad,
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoAd(
        url: String,
        requestObj: Req,
        expectObj: Res,
        principal: MkplPrincipalModel = MkplPrincipalModel(
            id = MkplAdStubBolts.AD_DEMAND_BOLT1.ownerId,
            groups = setOf(MkplUserGroups.TEST, MkplUserGroups.USER)
        )
    ) {
        webClient
            .post()
            .uri("/v1/ad/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTH_HEADER, principal.createJwtTestHeader())
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
