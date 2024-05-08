package ru.otus.otuskotlin.marketplace.blackbox.fixture.client

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import io.kotest.common.runBlocking
import kotlinx.coroutines.channels.Channel
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

/**
 * Клиент, работающий через rabbit-mq
 * Запросы уходят в $version-queue, а ответы читаются из $version-queue-out
 */
class RabbitClient(
    dockerCompose: DockerCompose,
) : Client {
    private val channel by lazy {
        Thread.sleep(20_000)
        ConnectionFactory().apply {
            val url = dockerCompose.inputUrl
            host = url.host
            port = url.port
            username = dockerCompose.user
            password = dockerCompose.password
        }.newConnection().createChannel()
    }
    private val coroChannelByVersion = mutableMapOf<String, Channel<String>>()

    private fun getCoroChannel(version: String): Channel<String> = coroChannelByVersion.computeIfAbsent(version) {
        val coroChannel = Channel<String>()

        val deliverCallback = DeliverCallback { consumerTag, delivery ->
            val responseJson = String(delivery.body, Charsets.UTF_8)
            println("Received in callback $version by $consumerTag:\n$responseJson")
            runBlocking {
                coroChannel.send(responseJson)
            }
        }

        channel.basicConsume("$version-queue-out", true, deliverCallback, CancelCallback { })

        coroChannel
    }

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val coroChannel = getCoroChannel(version)

        // выкинем элемент из канала (мало ли)
        coroChannel.tryReceive()

        println("Send $version:\n$request")
        channel.basicPublish("", "$version-queue", null, request.toByteArray())

        val response = coroChannel.receive()
        println("Received:\n$response")
        return response
    }
}
