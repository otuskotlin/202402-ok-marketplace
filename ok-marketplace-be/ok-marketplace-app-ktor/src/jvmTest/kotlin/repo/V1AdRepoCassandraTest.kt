package ru.otus.otuskotlin.marketplace.app.ktor.repo

import com.benasher44.uuid.uuid4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugMode
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.RepoAdCassandra
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import java.time.Duration

class V1AdRepoCassandraTest : V1AdRepoBaseTest() {
    override val workMode: AdRequestDebugMode = AdRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoAd) = MkplAppSettings(
        corSettings = MkplCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(repository("ks_create", uuidNew))
    )
    override val appSettingsRead: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            repository("ks_read"),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsUpdate: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            repository("ks_update", uuidNew),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsDelete: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            repository("ks_delete"),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsSearch: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            repository("ks_search"),
            initObjects = listOf(initAd),
        )
    )
    override val appSettingsOffers: MkplAppSettings = mkAppSettings(
        repo = AdRepoInitialized(
            repository("ks_offers"),
            initObjects = listOf(initAd, initAdSupply),
        )
    )

    companion object {
        class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")
        private val container by lazy {
            @Suppress("Since15")
            TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
        }

        @JvmStatic
        @BeforeClass
        fun tearUp() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            container.stop()
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
}
