package ru.otus.otuskotlin.marketplace.backend.repo.sql

actual class MkplAdSqlConn {
    val driver = PostgresDriver(
        host = "host.docker.internal",
        port = 5432,
        user = "postgres",
        database = "postgres",
        password = "postgres",
    )
}
