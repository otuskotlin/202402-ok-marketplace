package ru.otus.otuskotlin.marketplace.e2e.be.fixture.client

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

/**
 * Отправка запросов по http/rest
 */
class RestAuthClient(dockerCompose: DockerCompose) : Client {
    private val log = Logger
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp)
    private val token by lazy { runBlocking { getToken() } }
    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            path("$version/$path")
        }.build()

        val resp = client.post {
            url(url)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                append(HttpHeaders.Authorization, token)
            }
            accept(ContentType.Application.Json)
            setBody(request)

        }.call

        return resp.body()
    }

    private suspend fun getToken(): String {
        val url = urlBuilder.apply {
            path("/realms/otus-marketplace/protocol/openid-connect/token")
        }.build()
        val response = client.post(url) {
            setBody(
                FormDataContent(Parameters.build {
                    append("client_id", "otus-marketplace-service")
                    append("grant_type", "password")
                    append("username", "otus-test")
                    append("password", "otus")
                })
            )
        }
        val tokenObj: TokenApi = Json.decodeFromString(response.body())
        log.i { "TOKEN: ${tokenObj.accessToken}" }
        return "${tokenObj.tokenType} ${tokenObj.accessToken}"
    }

    @Serializable
    data class TokenApi(
        @SerialName("access_token")
        val accessToken: String = "",
        @SerialName("expires_in")
        val expiresIn: Int = 0,
        @SerialName("refresh_expires_in")
        val refreshExpiresIn: Int = 0,
        @SerialName("refresh_token")
        val refreshToken: String = "",
        @SerialName("token_type")
        val tokenType: String = "",
        @SerialName("not-before-policy")
        val notBeforePolicty: Int = 0,
        @SerialName("session_state")
        val sessionState: String = "",
        @SerialName("scope")
        val scope: String = "",
    )
}
