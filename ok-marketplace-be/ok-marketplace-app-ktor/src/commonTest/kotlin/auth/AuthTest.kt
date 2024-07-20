package ru.otus.otuskotlin.marketplace.app.ktor.auth

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.module
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        application { module(MkplAppSettings(corSettings = MkplCorSettings(repoTest = AdRepoInMemory()))) }
        val response = client.post("/v2/ad/create") {
            addAuth(groups = emptyList())
            contentType(ContentType.Application.Json)
            setBody(
                AdCreateRequest(
                    ad = AdCreateObject(
                        title = "xxsdgff",
                        description = "dfgdfg",
                        adType = DealSide.SUPPLY,
                        visibility = AdVisibility.PUBLIC,
                    ),
                    debug = AdDebug(mode = AdRequestDebugMode.TEST)
                )
            )
        }
        val adObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ResponseResult.ERROR, adObj.result)
        assertEquals("access-create", adObj.errors?.first()?.code)
    }
}
