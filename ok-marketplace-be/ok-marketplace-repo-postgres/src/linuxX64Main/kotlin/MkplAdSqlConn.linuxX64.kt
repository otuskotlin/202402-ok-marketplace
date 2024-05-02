package ru.otus.otuskotlin.marketplace.backend.repo.sql

import io.github.moreirasantos.pgkn.PostgresDriver

actual class MkplAdSqlConn {
    val driver = PostgresDriver(
        host = "host.docker.internal",
        port = 5432,
        user = "postgres",
        database = "postgres",
        password = "postgres",
    )
}
