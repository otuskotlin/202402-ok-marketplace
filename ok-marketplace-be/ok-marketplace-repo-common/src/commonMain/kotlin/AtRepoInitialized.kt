package ru.otus.otuskotlin.marketplace.repo.common

import ru.otus.otuskotlin.marketplace.common.models.MkplAd

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class AtRepoInitialized(
    private val repo: IRepoAdInitializable,
    private val initObjects: Collection<MkplAd> = emptyList(),
) : IRepoAdInitializable by repo {
    val initializedObjects: List<MkplAd> = save(initObjects).toList()
}
