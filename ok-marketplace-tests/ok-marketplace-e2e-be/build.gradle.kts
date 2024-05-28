plugins {
    id("build-jvm")
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("ru.otus.otuskotlin.marketplace:ok-marketplace-api-v1-jackson")
    implementation("ru.otus.otuskotlin.marketplace:ok-marketplace-api-v2-kmp")

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

//    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
//    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
    testImplementation(libs.kotlinx.serialization.core)
    testImplementation(libs.kotlinx.serialization.json)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-spring").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-be").task(":ok-marketplace-app-ktor:publishImageToLocalRegistry"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-rabbit").task("dockerBuildImage"))
//        dependsOn(gradle.includedBuild(":ok-marketplace-app-kafka").task("dockerBuildImage"))
    }
}
