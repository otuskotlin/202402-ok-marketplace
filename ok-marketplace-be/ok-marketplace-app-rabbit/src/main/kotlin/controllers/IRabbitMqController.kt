package ru.otus.otuskotlin.marketplace.app.rabbit.controllers

import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration

interface IRabbitMqController {
    val exchangeConfig: RabbitExchangeConfiguration
    suspend fun process()
    fun close()
}

