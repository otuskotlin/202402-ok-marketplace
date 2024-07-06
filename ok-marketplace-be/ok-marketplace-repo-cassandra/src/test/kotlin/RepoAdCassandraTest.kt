package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import java.time.Duration

class RepoAdCassandraCreateTest : RepoAdCreateTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_create", lockNew.asString())
    )
}

class RepoAdCassandraReadTest : RepoAdReadTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_read")
    )
}

class RepoAdCassandraUpdateTest : RepoAdUpdateTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_update", lockNew.asString())
    )
}

class RepoAdCassandraDeleteTest : RepoAdDeleteTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_delete")
    )
}

class RepoAdCassandraSearchTest : RepoAdSearchTest() {
    override val repo = AdRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_search")
    )
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(keyspace: String, uuid: String? = null): RepoAdCassandra {
        return RepoAdCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
        )
    }
}
