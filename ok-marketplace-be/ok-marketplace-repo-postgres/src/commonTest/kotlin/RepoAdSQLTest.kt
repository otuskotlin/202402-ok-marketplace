package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import kotlin.test.AfterTest

private fun IRepoAd.clear() {
    val pgRepo = (this as AdRepoInitialized).repo as RepoAdSql
    pgRepo.clear()
}

class RepoAdSQLCreateTest : RepoAdCreateTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLReadTest : RepoAdReadTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLUpdateTest : RepoAdUpdateTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoAdSQLDeleteTest : RepoAdDeleteTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLSearchTest : RepoAdSearchTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
