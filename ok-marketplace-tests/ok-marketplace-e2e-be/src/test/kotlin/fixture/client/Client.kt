package ru.otus.otuskotlin.marketplace.e2e.be.fixture.client

/**
 * Клиент к нашему приложению в докер-композе, который умеет отправлять запрос и получать ответ.
 * Способ отправки/получения зависит от приложения - rabbit, http, ws, ...
 */
interface Client {
    /**
     * @param version версия АПИ (v1)
     * @param path путь к ресурсу, имя топика и т.п. (ad/create)
     * @param request тело сообщения в виде строки
     * @return тело ответа
     */
    suspend fun sendAndReceive(version: String, path: String, request: String): String
}
