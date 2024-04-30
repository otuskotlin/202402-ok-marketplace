package ru.otus.otuskotlin.marketplace.common.repo.exceptions

import ru.otus.otuskotlin.marketplace.common.models.MkplAdId

class RepoEmptyLockException(id: MkplAdId): RepoAdException(
    id,
    "Lock is empty in DB"
)
