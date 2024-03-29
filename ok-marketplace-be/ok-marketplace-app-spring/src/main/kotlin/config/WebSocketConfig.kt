package ru.otus.otuskotlin.markeplace.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import ru.otus.otuskotlin.markeplace.app.spring.controllers.AdControllerV1Ws
import ru.otus.otuskotlin.markeplace.app.spring.controllers.AdControllerV2Ws


@Suppress("unused")
@Configuration
class WebSocketConfig(
    private val adControllerV1: AdControllerV1Ws,
    private val adControllerV2: AdControllerV2Ws,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/v1/ws" to adControllerV1,
            "/v2/ws" to adControllerV2,
        )
        return SimpleUrlHandlerMapping(handlerMap, 1)
    }
}
