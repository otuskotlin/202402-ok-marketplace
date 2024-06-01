package ru.otus.otuskotlin.marketplace.app.rabbit.config

// наш класс настроек взаимодействия с RMQ
data class RabbitExchangeConfiguration(
    val keyIn: String = "",
    val keyOut: String = "",
    // Отправляем сообщение в обменник
    val exchange: String = "",
    // Подписываемся на очередь
    val queue: String = "",
    val consumerTag: String = "",
    val exchangeType: String = "direct" // Объявляем обменник типа "direct" (сообщения передаются в те очереди, где ключ совпадает)
) {
    companion object {
        val NONE = RabbitExchangeConfiguration()
    }
}
