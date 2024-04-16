package ru.otus.otuskotlin.marketplace.biz.stubs

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubOffersSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для получения предложений для объявления
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            adResponse = MkplAdStub.prepareResult {
                adRequest.id.takeIf { it != MkplAdId.NONE }?.also { this.id = it }
            }
            adsResponse.addAll(MkplAdStub.prepareOffersList(adResponse.title, MkplDealSide.SUPPLY))
        }
    }
}
