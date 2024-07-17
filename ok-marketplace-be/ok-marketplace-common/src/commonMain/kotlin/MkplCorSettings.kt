package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSessionRepo
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.states.common.MkplStatesCorSettings

data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val wsSessions: IMkplWsSessionRepo = IMkplWsSessionRepo.NONE,
    val repoStub: IRepoAd = IRepoAd.NONE,
    val repoTest: IRepoAd = IRepoAd.NONE,
    val repoProd: IRepoAd = IRepoAd.NONE,
    val stateSettings: MkplStatesCorSettings = MkplStatesCorSettings(),
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
