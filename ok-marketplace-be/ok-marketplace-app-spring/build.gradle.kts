plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Внутренние модели
    implementation(project(":ok-marketplace-common"))
    implementation(project(":ok-marketplace-app-common"))
    implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-logback")

    // v1 api
    implementation(project(":ok-marketplace-api-v1-jackson"))
    implementation(project(":ok-marketplace-api-v1-mappers"))

    // v2 api
    implementation(project(":ok-marketplace-api-v2-kmp"))

    // biz
    implementation(project(":ok-marketplace-biz"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)

    // stubs
    testImplementation(project(":ok-marketplace-stubs"))
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1", "spec-v2").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
