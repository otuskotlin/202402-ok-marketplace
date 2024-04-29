package ru.otus.otuskotlin.marketplace.common.repo.exceptions

import ru.otus.otuskotlin.marketplace.common.models.MkplAdId

open class RepoAdException(
    @Suppress("unused")
    val adId: MkplAdId,
    msg: String,
): RepoException(msg)
