package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized

class RepoAdGremlinCreateTest : RepoAdCreateTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_create", lockNew.asString())
    )
}

class RepoAdGremlinReadTest : RepoAdReadTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_read")
    )
    override val readSucc = repo.initializedObjects[0]
}

class RepoAdGremlinUpdateTest : RepoAdUpdateTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_update", lockNew.asString())
    )
    override val updateSucc: MkplAd by lazy { repo.initializedObjects[0] }
    override val updateConc: MkplAd by lazy { repo.initializedObjects[1] }
}

class RepoAdGremlinDeleteTest : RepoAdDeleteTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_delete")
    )
    override val deleteSucc: MkplAd by lazy { repo.initializedObjects[0] }
    override val deleteConc: MkplAd by lazy { repo.initializedObjects[1] }
    override val notFoundId: MkplAdId = MkplAdId("#3100:0")
}

class RepoAdGremlinSearchTest : RepoAdSearchTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = ArcadeDbContainer.repository("test_search")
    )
    override val initializedObjects: List<MkplAd> = repo.initializedObjects
}
