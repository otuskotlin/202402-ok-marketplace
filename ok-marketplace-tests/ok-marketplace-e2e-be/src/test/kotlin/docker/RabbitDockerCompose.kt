package ru.otus.otuskotlin.marketplace.e2e.be.docker

import ru.otus.otuskotlin.marketplace.e2e.be.fixture.docker.AbstractDockerCompose

object RabbitDockerCompose : AbstractDockerCompose(
    "rabbit_1", 5672, "docker-compose-rabbit.yml"
) {
    override val user: String
        get() = "guest"
    override val password: String
        get() = "guest"
}
