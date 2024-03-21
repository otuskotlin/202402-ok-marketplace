package ru.otus.otuskotlin.markeplace.app.spring.config

import org.springdoc.core.configuration.SpringDocConfiguration
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.ObjectMapperProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// swagger url: http://localhost:8080/swagger-ui/index.html
@Suppress("unused")
@Configuration
class SwaggerConfiguration {
    @Bean
    fun springDocConfiguration() = SpringDocConfiguration()

    @Bean
    fun springDocConfigProperties() = SpringDocConfigProperties()

    @Bean
    fun objectMapperProvider(springDocConfigProperties: SpringDocConfigProperties) =
        ObjectMapperProvider(springDocConfigProperties)
}
