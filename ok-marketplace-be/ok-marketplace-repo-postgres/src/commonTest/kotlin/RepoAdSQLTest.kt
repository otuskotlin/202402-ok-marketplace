package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import kotlin.random.Random

val random = Random(Clock.System.now().nanosecondsOfSecond)
class RepoAdSQLCreateTest : RepoAdCreateTest() {
    override val repo: IRepoAd = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSQLDeleteTest : RepoAdDeleteTest() {
    override val repo: IRepoAd = SqlTestCompanion.repoUnderTestContainer(
        "delete_" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLReadTest : RepoAdReadTest() {
    override val repo: IRepoAd = SqlTestCompanion.repoUnderTestContainer(
        "read" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLSearchTest : RepoAdSearchTest() {
    override val repo: IRepoAd = SqlTestCompanion.repoUnderTestContainer(
        "search" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLUpdateTest : RepoAdUpdateTest() {
    override val repo: IRepoAd = SqlTestCompanion.repoUnderTestContainer(
        "update" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}
