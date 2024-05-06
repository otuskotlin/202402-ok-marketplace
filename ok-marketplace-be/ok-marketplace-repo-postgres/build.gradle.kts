import com.bmuschko.gradle.docker.tasks.container.*
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.Frame
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

plugins {
    id("build-kmp")
    alias(libs.plugins.muschko.remote)
    alias(libs.plugins.liquibase)
}
repositories {
    google()
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        "classpath"(libs.testcontainers.postgres)
        "classpath"(libs.db.postgres)
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.okMarketplaceCommon)
                api(projects.okMarketplaceRepoCommon)

                implementation(libs.coroutines.core)
                implementation(libs.uuid)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.okMarketplaceRepoTests)
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(libs.db.postgres)
//                implementation(libs.db.hikari)
                implementation(libs.bundles.exposed)
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        nativeMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        linuxX64Main {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("io.github.moreirasantos:pgkn:1.1.0")
            }
        }
    }
}

dependencies {
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.liquibase.snakeyml)
    liquibaseRuntime(libs.db.postgres)
}

var pgPort = 5432
val pgDbName = "marketplace_ads"
val pgUsername = "postgres"
val pgPassword = "marketplace-pass"
val containerStarted = AtomicBoolean(false)
val pgContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
    withUsername(pgUsername)
    withPassword(pgPassword)
    withDatabaseName(pgDbName)
    this.startupCheckStrategy
//    waitingFor(Wait.forLogMessage("database system is ready to accept connections", 1))
}

tasks {
    // Здесь в тасках запускаем PotgreSQL в контейнере
    // Накатываем liquibase миграцию
    // Передаем настройки в среду тестирования
    val postgresImage = "postgres:latest"
    val pullImage by creating(DockerPullImage::class) {
        image.set(postgresImage)
    }
    val dbContainer by creating(DockerCreateContainer::class) {
        dependsOn(pullImage)
        targetImageId(pullImage.image)
        withEnvVar("POSTGRES_PASSWORD", pgPassword)
        withEnvVar("POSTGRES_USER", pgUsername)
        withEnvVar("POSTGRES_DB", pgDbName)
        healthCheck.cmd("pg_isready")
        hostConfig.portBindings.set(listOf(":5432"))
        exposePorts("tcp", listOf(5432))
        hostConfig.autoRemove.set(true)
    }
    val stopPg by creating(DockerStopContainer::class) {
        targetContainerId(dbContainer.containerId)
    }
    val startPg by creating(DockerStartContainer::class) {
        dependsOn(dbContainer)
        targetContainerId(dbContainer.containerId)
        finalizedBy(stopPg)
    }
    val inspectPg by creating(DockerInspectContainer::class) {
        dependsOn(startPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.containerId)
        onNext(
            object : Action<InspectContainerResponse> {
                override fun execute(container: InspectContainerResponse) {
                    pgPort = container.networkSettings.ports.bindings[ExposedPort.tcp(5432)]
                        ?.first()
                        ?.hostPortSpec
                        ?.toIntOrNull()
                        ?: throw Exception("Postgres port is not found in container")
                }
            }
        )
    }
    val waitingPg by creating(DockerLogsContainer::class) {
        dependsOn(inspectPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.containerId)
        tailAll = true
        follow = true
        onNext {
            object : Action<Frame> {
                override fun execute(container: Frame) {
                    println("CLAZZ: ${container.toString()}")

                }
            }
        }
    }
    val pgStop by creating {
        doFirst {
            pgContainer.stop()
        }
    }
    val pgStart by creating {
        finalizedBy(pgStop)
        doFirst {
            pgContainer.start()
        }
    }
    val liquibaseUpdate = getByName("update") {
//        dependsOn(inspectPg)
//        finalizedBy(stopPg)
        dependsOn(pgStart)
        finalizedBy(pgStop)
        doFirst {
            println("waiting for a while ${System.currentTimeMillis()/1000000}")
            Thread.sleep(30000)
            println("LQB: \"jdbc:postgresql://localhost:$pgPort/$pgDbName\" ${System.currentTimeMillis()/1000000}")
            liquibase {
                activities {
                    register("main") {
                        arguments = mapOf(
                            "logLevel" to "info",
                            "searchPath" to layout.projectDirectory.dir("migrations").asFile.toString(),
                            "changelogFile" to "changelog-v0.0.1.sql",
//                            "url" to "jdbc:postgresql://localhost:$pgPort/$pgDbName",
                            "url" to pgContainer.jdbcUrl,
                            "username" to pgUsername,
                            "password" to pgPassword,
                            "driver" to "org.postgresql.Driver"
                        )
                    }
                }
            }
        }
    }
    val waitPg by creating(DockerWaitContainer::class) {
        dependsOn(inspectPg)
        dependsOn(liquibaseUpdate)
        containerId.set(startPg.containerId)
        finalizedBy(stopPg)
        doFirst {
            println("PORT: $pgPort")
        }
    }
    withType(KotlinNativeTest::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
    withType(Test::class) {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
}
