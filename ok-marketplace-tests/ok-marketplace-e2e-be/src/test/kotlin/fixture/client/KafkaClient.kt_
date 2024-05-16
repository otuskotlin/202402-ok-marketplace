package ru.otus.otuskotlin.marketplace.blackbox.fixture.client

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose
import java.time.Duration
import java.util.UUID

/**
 * Отправка запросов в очереди kafka
 */
class KafkaClient(dockerCompose: DockerCompose) : Client {
    private val host by lazy {
        val url = dockerCompose.inputUrl
        "${url.host}:${url.port}"
    }
    private val producer by lazy {
        KafkaProducer<String, String>(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to host,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
            )
        )
    }
    private val consumer by lazy {
        KafkaConsumer<String, String>(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to host,
                ConsumerConfig.GROUP_ID_CONFIG to UUID.randomUUID().toString(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
            )
        ).also {
            it.subscribe(versions.map { "marketplace-out-$it" })
        }
    }
    private var counter = 0
    private val versions = setOf("v1", "v2")

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        if (version !in versions) {
            throw UnsupportedOperationException("Unknown version $version")
        }

        counter += 1
        producer.send(ProducerRecord("marketplace-in-$version", "test-$counter", request)).get()

        val read = consumer.poll(Duration.ofSeconds(20))
        return read.firstOrNull()?.value() ?: ""
    }
}