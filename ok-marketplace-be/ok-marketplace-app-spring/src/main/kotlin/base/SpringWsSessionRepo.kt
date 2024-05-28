package ru.otus.otuskotlin.markeplace.app.spring.base

import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSession
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSessionRepo

class SpringWsSessionRepo: IMkplWsSessionRepo {
    private val sessions: MutableSet<IMkplWsSession> = mutableSetOf()
    override fun add(session: IMkplWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IMkplWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
