package ru.otus.otuskotlin.marketplace.common.ws

interface IMkplWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IMkplWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
