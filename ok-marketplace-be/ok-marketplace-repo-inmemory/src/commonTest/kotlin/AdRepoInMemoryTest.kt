import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.repo.common.AtRepoInitialized
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

class AdRepoInMemoryCreateTest : RepoAdCreateTest() {
    override val repo = AtRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryDeleteTest : RepoAdDeleteTest() {
    override val repo = AtRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryReadTest : RepoAdReadTest() {
    override val repo = AtRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemorySearchTest : RepoAdSearchTest() {
    override val repo = AtRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryUpdateTest : RepoAdUpdateTest() {
    override val repo = AtRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}
