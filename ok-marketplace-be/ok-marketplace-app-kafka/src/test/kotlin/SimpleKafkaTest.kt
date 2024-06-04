package ru.otus.otuskotlin.marketplace.app.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * Для запуска этого теста, требуется поднять Кафку на порту 9092.
 * Поднять можно с помощью /deploy/docker-compose-kafka-cp.yml
 */
@Ignore
class SimpleKafkaTest {
    private val topicName = "producer-topic"

    /**
     * Отправка сообщения в Кафку
     */
    @Test
    fun producerTest() {
        // create instance for properties to access producer configs
        val props = Properties().apply {
            //Assign localhost id
            put("bootstrap.servers", "localhost:9092")
            //Set acknowledgements for producer requests.
            put("acks", "all")
            //If the request fails, the producer can automatically retry,
            put("retries", 0)
            //Specify buffer size in config
            put("batch.size", 16384)
            //The buffer.memory controls the total amount of memory available to the producer for buffering.
            put("buffer.memory", 33554432)
            put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        }

        KafkaProducer<String, String>(props).use { producer ->
            (0..<10).forEach {
                val key = "key#$it"
                val value = "Message numbere $it"
                producer.send(ProducerRecord(topicName, key, value))
            }
            println("Message sent successfully")
        }
    }

    /**
     * Получение сообщения из топика Кафки
     */
    @Test
    fun consumerTest() {
        val props = Properties().apply {
            put("bootstrap.servers", "localhost:9092")
            // Here the consumer group should be set
            put("group.id", "test")
            put("enable.auto.commit", "true")
            put("auto.commit.interval.ms", "1000")
            put("session.timeout.ms", "30000")
            // https://kafka.apache.org/documentation/#consumerconfigs_auto.offset.reset
            put("auto.offset.reset", "earliest")
            put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
            put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        }
        KafkaConsumer<String, String>(props).use { consumer ->

            //Kafka Consumer subscribes list of topics here.
            consumer.subscribe(listOf(topicName))
            val timeWithTimeout = Instant.now() + Duration.ofSeconds(2)

            while (timeWithTimeout > Instant.now()) {
                val records = consumer.poll(Duration.ofMillis(100))
                records.forEach { record ->
                    // print the offset,key and value for the consumer records.
                    println("topic = ${record.topic()}, offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
                }
            }
        }
    }
}
