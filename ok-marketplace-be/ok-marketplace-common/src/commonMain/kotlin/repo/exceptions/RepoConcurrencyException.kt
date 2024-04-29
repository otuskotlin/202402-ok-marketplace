package ru.otus.otuskotlin.marketplace.common.repo.exceptions

import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplAdLock

class RepoConcurrencyException(id: MkplAdId, expectedLock: MkplAdLock, actualLock: MkplAdLock?): RepoAdException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
