package ru.otus.otuskotlin.marketplace.e2e.be.test

import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.marketplace.e2e.be.docker.WiremockDockerCompose
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.BaseFunSpec
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose
import ru.otus.otuskotlin.marketplace.e2e.be.docker.KtorJvmPGDockerCompose
import ru.otus.otuskotlin.marketplace.e2e.be.docker.KtorLinuxPGDockerCompose
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.RestClient
import ru.otus.otuskotlin.marketplace.e2e.be.test.action.v1.toV1
import ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2.toV2

enum class TestDebug {
    STUB, PROD, TEST
}

// Kotest не сможет подставить правильный аргумент конструктора, поэтому
// нужно запретить ему запускать этот класс
@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose, debug: TestDebug = TestDebug.STUB) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
    testApiV2(restClient, prefix = "rest ", debug = debug.toV2())
})

class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)

//class AccRestSpringTest : AccRestTestBase(SpringDockerCompose)
class AccRestKtorPgJvmTest : AccRestTestBase(KtorJvmPGDockerCompose, debug = TestDebug.PROD)
class AccRestKtorPgLinuxTest : AccRestTestBase(KtorLinuxPGDockerCompose, debug = TestDebug.PROD)
