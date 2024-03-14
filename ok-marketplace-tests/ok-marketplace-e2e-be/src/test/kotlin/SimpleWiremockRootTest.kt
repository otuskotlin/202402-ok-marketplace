package ru.otus.otuskotlin.marketplace.e2e.be

import co.touchlab.kermit.Logger
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

private val log = Logger

/**
 * Простейший демонстрационный тест для запуска с использованием wiremock
 * Предназначен для первоначального ознакомления со стеком
 */
@OptIn(ExperimentalKotest::class)
class SimpleWiremockRootTest : StringSpec({
    this.blockingTest = true
    isolationMode = IsolationMode.SingleInstance
    beforeSpec { start() }

    "Root GET method is working" {
        val client = HttpClient(OkHttp)
        val response = client.get(getUrl().buildString())
        val bodyString = response.call.response.bodyAsText()
        bodyString shouldBe "Hello, world!"
    }

    afterSpec { stop() }
}) {
    companion object {
        private val service = "app-wiremock_1"
        private val port = 8080

        private val compose by lazy {
            DockerComposeContainer(File("docker-compose/docker-compose-wiremock.yml")).apply {
                withOptions("--compatibility")
                withExposedService(service, port)
                withLocalCompose(true)
            }
        }

        fun start() {
            kotlin.runCatching { compose.start() }.onFailure {
                log.e { "Failed to start wiremock" }
                throw it
            }
            log.w("\n=========== wiremock started =========== \n")
        }

        fun stop() {
            compose.close()
            log.w("\n=========== wiremock complete =========== \n")
        }

        private fun getUrl() = URLBuilder(
            protocol = URLProtocol.HTTP,
            host = compose.getServiceHost(service, port),
            port = compose.getServicePort(service, port),
        )

    }
}
