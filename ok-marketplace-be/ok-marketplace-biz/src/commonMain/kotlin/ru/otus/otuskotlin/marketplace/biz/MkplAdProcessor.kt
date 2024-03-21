package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub

@Suppress("unused", "RedundantSuspendModifier")
class MkplAdProcessor(val corSettings: MkplCorSettings) {
    suspend fun exec(ctx: MkplContext) {
        ctx.adResponse = MkplAdStub.get()
        ctx.state = MkplState.RUNNING
    }
}
