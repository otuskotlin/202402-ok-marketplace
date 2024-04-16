package ru.otus.otuskotlin.marketplace.app.ktor.stub

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.module
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V2AdStubApiTest {

    @Test
    fun create() = v2TestApplication(
        func = "create",
        request = AdCreateRequest(
            ad = AdCreateObject(
                title = "Болт",
                description = "КРУТЕЙШИЙ",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC,
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun read() = v2TestApplication(
        func = "read",
        request = AdReadRequest(
            ad = AdReadObject("666"),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun update() = v2TestApplication(
        func = "update",
        request = AdUpdateRequest(
            ad = AdUpdateObject(
                id = "666",
                title = "Болт",
                description = "КРУТЕЙШИЙ",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC,
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun delete() = v2TestApplication(
        func = "delete",
        request = AdDeleteRequest(
            ad = AdDeleteObject(
                id = "666",
                lock = "123"
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun search() = v2TestApplication(
        func = "search",
        request = AdSearchRequest(
            adFilter = AdSearchFilter(),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.ads?.first()?.id)
    }

    @Test
    fun offers() = v2TestApplication(
        func = "offers",
        request = AdOffersRequest(
            ad = AdReadObject(
                id = "666"
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<AdOffersResponse>()
        assertEquals(200, response.status.value)
        assertEquals("s-666-01", responseObj.ads?.first()?.id)
    }

    private inline fun <reified T: IRequest> v2TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(MkplAppSettings(corSettings = MkplCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/ad/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
