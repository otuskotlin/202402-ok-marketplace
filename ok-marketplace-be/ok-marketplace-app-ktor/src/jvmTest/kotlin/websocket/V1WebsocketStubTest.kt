package ru.otus.otuskotlin.marketplace.app.ktor.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.moduleJvm
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = AdCreateRequest(
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
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = AdReadRequest(
            ad = AdReadObject("666"),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun updateStub() {
        val request = AdUpdateRequest(
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
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = AdDeleteRequest(
            ad = AdDeleteObject(
                id = "666",
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = AdSearchRequest(
            adFilter = AdSearchFilter(),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun offersStub() {
        val request = AdOffersRequest(
            ad = AdReadObject(
                id = "666",
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { moduleJvm(MkplAppSettings(corSettings = MkplCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<AdInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}
