package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<MkplAd> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ) = AdRepoInitialized(
        repo = RepoAdSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

