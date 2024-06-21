package ru.otus.otuskotlin.marketplace.repo.common

import ru.otus.otuskotlin.marketplace.common.models.MkplAd

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class AdRepoInitialized(
    private val repo: IRepoAdInitializable,
    initObjects: Collection<MkplAd> = emptyList(),
) : IRepoAdInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MkplAd> = save(initObjects).toList()
}
