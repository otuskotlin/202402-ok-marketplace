package ru.otus.otuskotlin.marketplace.app.kafka

import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportAd
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.common.MkplContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: MkplContext): String {
        val response: IResponse = source.toTransportAd()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MkplContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
