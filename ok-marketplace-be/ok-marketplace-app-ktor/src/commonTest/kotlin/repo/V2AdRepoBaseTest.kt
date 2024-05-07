package ru.otus.otuskotlin.marketplace.app.ktor.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportCreate
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportDelete
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportRead
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportUpdate
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.module
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplAdLock
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V2AdRepoBaseTest {
    abstract val workMode: AdRequestDebugMode
    abstract val appSettingsCreate: MkplAppSettings
    abstract val appSettingsRead:   MkplAppSettings
    abstract val appSettingsUpdate: MkplAppSettings
    abstract val appSettingsDelete: MkplAppSettings
    abstract val appSettingsSearch: MkplAppSettings
    abstract val appSettingsOffers: MkplAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val uuidSup = "10000000-0000-0000-0000-000000000003"
    protected val initAd = MkplAdStub.prepareResult {
        id = MkplAdId(uuidOld)
        adType = MkplDealSide.DEMAND
        lock = MkplAdLock(uuidOld)
    }
    protected val initAdSupply = MkplAdStub.prepareResult {
        id = MkplAdId(uuidSup)
        adType = MkplDealSide.SUPPLY
    }


    @Test
    fun create() {
        val ad = initAd.toTransportCreate()
        v2TestApplication(
            conf = appSettingsCreate,
            func = "create",
            request = AdCreateRequest(
                ad = ad,
                debug = AdDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<AdCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.ad?.id)
            assertEquals(ad.title, responseObj.ad?.title)
            assertEquals(ad.description, responseObj.ad?.description)
            assertEquals(ad.adType, responseObj.ad?.adType)
            assertEquals(ad.visibility, responseObj.ad?.visibility)
        }
    }

    @Test
    fun read() {
        val ad = initAd.toTransportRead()
        v2TestApplication(
            conf = appSettingsRead,
            func = "read",
            request = AdReadRequest(
                ad = ad,
                debug = AdDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<AdReadResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.ad?.id)
        }
    }

    @Test
    fun update() {
        val ad = initAd.toTransportUpdate()
        v2TestApplication(
            conf = appSettingsUpdate,
            func = "update",
            request = AdUpdateRequest(
                ad = ad,
                debug = AdDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<AdUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(ad.id, responseObj.ad?.id)
            assertEquals(ad.title, responseObj.ad?.title)
            assertEquals(ad.description, responseObj.ad?.description)
            assertEquals(ad.adType, responseObj.ad?.adType)
            assertEquals(ad.visibility, responseObj.ad?.visibility)
            assertEquals(uuidNew, responseObj.ad?.lock)
        }
    }
    @Test
    fun delete() {
        val ad = initAd.toTransportDelete()
        v2TestApplication(
            conf = appSettingsDelete,
            func = "delete",
            request = AdDeleteRequest(
                ad = ad,
                debug = AdDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<AdDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.ad?.id)
        }
    }

    @Test
    fun search() = v2TestApplication(
        conf = appSettingsSearch,
        func = "search",
        request = AdSearchRequest(
            adFilter = AdSearchFilter(),
            debug = AdDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(uuidOld, responseObj.ads?.first()?.id)
    }

    @Test
    fun offers() = v2TestApplication(
        conf = appSettingsOffers,
        func = "offers",
        request = AdOffersRequest(
            ad = initAd.toTransportRead(),
            debug = AdDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<AdOffersResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(uuidSup, responseObj.ads?.first()?.id)
    }

    private inline fun <reified T: IRequest> v2TestApplication(
        conf: MkplAppSettings,
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(appSettings = conf) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/ad/$func") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
