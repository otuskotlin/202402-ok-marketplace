package ru.otus.otuskotlin.marketplace.blackbox.fixture.docker

import io.ktor.http.URLBuilder

/**
 * Это обертка над сервисами в docker-compose. Позволяет их запускать и останавливать,
 * получать url для отправки запросов и очищать БД (чтобы между тестами не делать пересоздание контейнеров)
 */
interface DockerCompose {
    fun start()
    fun stop()

    /**
     * Очищает БД (возвращает ее к начальному состоянию)
     */
    fun clearDb()

    /**
     * URL для отправки запросов
     */

    val inputUrl: URLBuilder

    /**
     * Пользователь для подключения (доступен не везде)
     */
    val user: String get() = throw UnsupportedOperationException("no user")
    /**
     * Пароль для подключения (доступен не везде)
     */
    val password: String get() = throw UnsupportedOperationException("no password")

}