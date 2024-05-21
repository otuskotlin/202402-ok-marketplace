package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration

object ArcadeDbContainer {
    val username: String = "root"
    val password: String = "root_root"
    val container by lazy {
        GenericContainer(DockerImageName.parse("arcadedata/arcadedb:${ARCADEDB_VERSION}")).apply {
            withExposedPorts(2480, 2424, 8182)
            withEnv(
                "JAVA_OPTS", "-Darcadedb.server.rootPassword=$password " +
                        "-Darcadedb.server.plugins=GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin"
            )
            waitingFor(Wait.forLogMessage(".*ArcadeDB Server started.*\\n", 1))
            withStartupTimeout(Duration.ofMinutes(5))
            start()
            println("ARCADE: http://${host}:${getMappedPort(2480)}")
            println("ARCADE: http://${host}:${getMappedPort(2424)}")
            println(this.logs)
            println("RUNNING?: ${this.isRunning}")
        }
    }

    fun repository(
        @Suppress("UNUSED_PARAMETER") db: String,
        uuid: String? = null
    ): AdRepoGremlin {
        return AdRepoGremlin(
            hosts = container.host,
            port = container.getMappedPort(8182),
            enableSsl = false,
            user = username,
            pass = password,
            graph = "graph", // сюда должно бы встать значение аргумента db, но для этого нужно настраивать БД
            randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
            initRepo = { g -> g.V().drop().iterate() },
        )
    }

}
