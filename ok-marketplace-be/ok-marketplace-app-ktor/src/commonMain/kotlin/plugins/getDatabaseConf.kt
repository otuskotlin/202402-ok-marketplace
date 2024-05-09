package ru.otus.otuskotlin.marketplace.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.ktor.configs.PostgresConfig
import ru.otus.otuskotlin.marketplace.backend.repo.postgresql.RepoAdSql
import ru.otus.otuskotlin.marketplace.backend.repo.postgresql.SqlProperties
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

expect fun Application.getDatabaseConf(type: AdDbType): IRepoAd

enum class AdDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.initInMemory(): IRepoAd {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return AdRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}


fun Application.initPostgres(): IRepoAd {
    val config = PostgresConfig(environment.config)
    return RepoAdSql(
        properties = SqlProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
            schema = config.schema,
            database = config.database,
        )
    )
}
