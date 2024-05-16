package ru.otus.otuskotlin.marketplace.e2e.be.fixture

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

/**
 * Базовая реализация тестов, которая выполняет запуск и останов контейнеров, а также очистку БД.
 * Основана на FunSpec
 */
abstract class BaseFunSpec(
    private val dockerCompose: DockerCompose,
    body: FunSpec.() -> Unit
) : FunSpec(body) {

    override suspend fun beforeSpec(spec: Spec) {
        dockerCompose.start()
    }

    override suspend fun afterSpec(spec: Spec) {
        dockerCompose.stop()
    }

    override suspend fun beforeEach(testCase: TestCase) {
        dockerCompose.clearDb()
    }
}
