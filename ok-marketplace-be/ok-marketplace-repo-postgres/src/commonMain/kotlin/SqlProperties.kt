package ru.otus.otuskotlin.marketplace.backend.repo.sql

data class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/marketplace",
    val user: String = "postgres",
    val password: String = "marketplace-pass",
    val schema: String = "marketplace",
    val table: String = "ad",
)
