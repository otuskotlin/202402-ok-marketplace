package ru.otus.otuskotlin.marketplace.app.ktor.repo

import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugMode
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

class V1AdRepoInmemoryTest : V1AdRepoBaseTest() {
    override val workMode: AdRequestDebugMode = AdRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoAd) = MkplAppSettings(
        corSettings = MkplCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(AdRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            AdRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsUpdate: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            AdRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsDelete: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            AdRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsSearch: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            AdRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsOffers: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            AdRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initAd, initAdSupply),
        )
    )
}
