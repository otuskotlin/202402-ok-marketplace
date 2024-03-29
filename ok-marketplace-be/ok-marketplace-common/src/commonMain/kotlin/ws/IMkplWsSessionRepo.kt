package ru.otus.otuskotlin.marketplace.common.ws

interface IMkplWsSessionRepo {
    fun add(session: IMkplWsSession)
    fun clearAll()
    fun remove(session: IMkplWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IMkplWsSessionRepo {
            override fun add(session: IMkplWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IMkplWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
