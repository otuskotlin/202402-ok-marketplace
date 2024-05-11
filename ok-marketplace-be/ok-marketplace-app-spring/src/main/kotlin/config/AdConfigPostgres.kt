package ru.otus.otuskotlin.markeplace.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.otus.otuskotlin.marketplace.backend.repo.postgresql.SqlProperties

// Так не работает
//@ConfigurationProperties(prefix = "markeplace.repository.psql")
@ConfigurationProperties(prefix = "psql")
data class AdConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "marketplace-pass",
    var database: String = "marketplace_ads",
    var schema: String = "public",
    var table: String = "ads",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
