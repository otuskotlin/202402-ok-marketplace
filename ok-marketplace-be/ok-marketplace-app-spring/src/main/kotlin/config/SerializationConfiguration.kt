package ru.otus.otuskotlin.markeplace.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper

@Suppress("unused")
@Configuration
class SerializationConfiguration {
    @Bean
    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(apiV2Mapper)
    }
}
