package ru.otus.otuskotlin.marketplace.e2e.be.fixture.docker

import co.touchlab.kermit.Logger
import io.ktor.http.*
import org.testcontainers.containers.DockerComposeContainer
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose
import java.io.File

private val log = Logger

/**
 * apps - список приложений в docker-compose. Первое приложение - "главное", его url возвращается как inputUrl
 * (например ваш сервис при работе по rest или брокер сообщений при работе с брокером)
 * dockerComposeName - имя docker-compose файла (относительно ok-marketplace-acceptance/docker-compose)
 */
abstract class AbstractDockerCompose(
    private val apps: List<AppInfo>,
    private val dockerComposeName: String)
: DockerCompose {

    constructor(service: String, port: Int, dockerComposeName: String)
        : this(listOf(AppInfo(service, port)), dockerComposeName)
    private fun getComposeFile(): File {
        val file = File("docker-compose/$dockerComposeName")
        if (!file.exists()) throw IllegalArgumentException("file $dockerComposeName not found!")
        return file
    }

    private val compose =
        DockerComposeContainer(getComposeFile()).apply {
            withOptions("--compatibility")
            apps.forEach { (service, port) ->
                withExposedService(
                    service,
                    port,
                )
            }
            withLocalCompose(true)
        }

    override fun start() {
        kotlin.runCatching { compose.start() }.onFailure {
            log.e { "Failed to start $dockerComposeName" }
            throw it
        }

        log.w("\n=========== $dockerComposeName started =========== \n")
        apps.forEachIndexed { index, _ ->
            log.i { "Started docker-compose with App at: ${getUrl(index)}" }
        }
    }

    override fun stop() {
        compose.close()
        log.w("\n=========== $dockerComposeName complete =========== \n")
    }

    override fun clearDb() {
        log.w("===== clearDb =====")
        // TODO сделать очистку БД, когда до этого дойдет
    }

    override val inputUrl: URLBuilder
        get() = getUrl(0)

    fun getUrl(no: Int) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = apps[no].let { compose.getServiceHost(it.service, it.port) },
        port = apps[no].let { compose.getServicePort(it.service, it.port) },
    )
    data class AppInfo(
        val service: String,
        val port: Int,
    )

}
