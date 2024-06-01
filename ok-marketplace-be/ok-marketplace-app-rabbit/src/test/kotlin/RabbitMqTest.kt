package ru.otus.otuskotlin.marketplace.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateObject
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.AdDebug
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugMode
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugStubs
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateObject as AdCreateObjectV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateRequest as AdCreateRequestV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateResponse as AdCreateResponseV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdDebug as AdDebugV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugMode as AdRequestDebugModeV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugStubs as AdRequestDebugStubsV2

//  тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
//                withExposedPorts(5672, 15672) // Для 3-management
                withExposedPorts(RMQ_PORT)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
//            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = MkplAppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
//        corSettings = MkplCorSettings(loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }),
        controllersConfigV1 = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = exchange,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = exchangeType
        ),
        controllersConfigV2 = RabbitExchangeConfiguration(
            keyIn = "in-v2",
            keyOut = "out-v2",
            exchange = exchange,
            queue = "v2-queue",
            consumerTag = "v2-consumer-test",
            exchangeType = exchangeType
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun adCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV1) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, AdCreateResponse::class.java)
                val expected = MkplAdStub.get()

                assertEquals(expected.title, response.ad?.title)
                assertEquals(expected.description, response.ad?.description)
            }
        }
    }

    @Test
    fun adCreateTestV2() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV2) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV2RequestSerialize(boltCreateV2).toByteArray())

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV2ResponseDeserialize<AdCreateResponseV2>(responseJson)
                val expected = MkplAdStub.get()
                assertEquals(expected.title, response.ad?.title)
                assertEquals(expected.description, response.ad?.description)
            }
        }
    }

    private val boltCreateV1 = with(MkplAdStub.get()) {
        AdCreateRequest(
            ad = AdCreateObject(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )
    }

    private val boltCreateV2 = with(MkplAdStub.get()) {
        AdCreateRequestV2(
            ad = AdCreateObjectV2(
                title = title,
                description = description
            ),
            debug = AdDebugV2(
                mode = AdRequestDebugModeV2.STUB,
                stub = AdRequestDebugStubsV2.SUCCESS
            )
        )
    }
}
