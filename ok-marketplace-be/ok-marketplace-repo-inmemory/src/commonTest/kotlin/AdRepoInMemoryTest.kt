import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

class AdRepoInMemoryCreateTest : RepoAdCreateTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryDeleteTest : RepoAdDeleteTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryReadTest : RepoAdReadTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemorySearchTest : RepoAdSearchTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryUpdateTest : RepoAdUpdateTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
