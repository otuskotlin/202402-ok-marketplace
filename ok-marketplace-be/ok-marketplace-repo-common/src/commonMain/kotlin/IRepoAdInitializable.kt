package ru.otus.otuskotlin.marketplace.repo.common

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd

interface IRepoAdInitializable: IRepoAd {
    fun save(ads: Collection<MkplAd>): Collection<MkplAd>
}
