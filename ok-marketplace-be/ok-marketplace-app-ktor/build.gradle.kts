import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import io.ktor.plugin.features.*
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    alias(libs.plugins.kotlinx.serialization)
    id("build-kmp")
//    id("io.ktor.plugin")
    alias(libs.plugins.ktor)
//    id("com.bmuschko.docker-remote-api") - Универсальный плагин для докера
    alias(libs.plugins.muschko.remote)
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    configureNativeImage(project)
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_21)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}

kotlin {
    // !!! Обязательно. Иначе не проходит сборка толстых джанриков в shadowJar
    jvm { withJava() }
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "ru.otus.otuskotlin.marketplace.app.ktor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.yaml)
                implementation(libs.ktor.server.negotiation)
                implementation(libs.ktor.server.headers.response)
                implementation(libs.ktor.server.headers.caching)
                implementation(libs.ktor.server.websocket)

//                // Для того, чтоб получать содержимое запроса более одного раза
//                В Application.main добавить `install(DoubleReceive)`
//                implementation("io.ktor:ktor-server-double-receive:${libs.versions.ktor.get()}")

                implementation(project(":ok-marketplace-common"))
                implementation(project(":ok-marketplace-app-common"))
                implementation(project(":ok-marketplace-biz"))

                // v2 api
                implementation(project(":ok-marketplace-api-v2-kmp"))

                // Stubs
                implementation(project(":ok-marketplace-stubs"))
                // RabbitMQ
//                implementation(project(":ok-marketplace-app-rabbit"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)

                // logging
                implementation(project(":ok-marketplace-api-log1"))
                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-common")
                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-kermit")
                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-socket")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // jackson
                implementation(libs.ktor.serialization.jackson)
                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)

                implementation(libs.logback)

                // transport models
                implementation(project(":ok-marketplace-api-v1-jackson"))
                implementation(project(":ok-marketplace-api-v1-mappers"))

                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-logback")

            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
    }

    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFileX64 = linkReleaseExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)

    val dockerDockerfileX64 by creating(Dockerfile::class) {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        from(Dockerfile.From("ubuntu:22.04").withPlatform("linux/amd64"))
        doFirst {
            copy {
                from(nativeFileX64)
                from(linuxX64ProcessResources.destinationDir)
                into("${this@creating.destDir.get()}")
            }
        }
        copyFile(nativeFileX64.name, "/app/")
        copyFile("application.yaml", "/app/")
        exposePort(8080)
        workingDir("/app")
        entryPoint("/app/${nativeFileX64.name}", "-config=./application.yaml")
    }
    val registryUser: String? = System.getenv("CONTAINER_REGISTRY_USER")
    val registryPass: String? = System.getenv("CONTAINER_REGISTRY_PASS")
    val registryHost: String? = System.getenv("CONTAINER_REGISTRY_HOST")
    val registryPref: String? = System.getenv("CONTAINER_REGISTRY_PREF")
    val imageName = registryPref?.let { "$it/${project.name}" } ?: project.name

    val dockerBuildX64Image by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfileX64)
        images.add("$imageName-x64:${rootProject.version}")
        images.add("$imageName-x64:latest")
        platform.set("linux/amd64")
    }
    val dockerPushX64Image by creating(DockerPushImage::class) {
        group = "docker"
        dependsOn(dockerBuildX64Image)
        images.set(dockerBuildX64Image.images)
        registryCredentials {
            username.set(registryUser)
            password.set(registryPass)
            url.set("https://$registryHost/v1/")
        }
    }
}
