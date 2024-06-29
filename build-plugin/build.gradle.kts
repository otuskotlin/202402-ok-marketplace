plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginMultiplatform"
        }
        register("build-pgContainer") {
            id = "build-pgContainer"
            implementationClass = "ru.otus.otuskotlin.marketplace.plugin.BuildPluginPgContainer"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // enable Ktlint formatting
//    add("detektPlugins", libs.plugin.detektFormatting)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.kotlin)
//    implementation(libs.plugin.dokka)
    implementation(libs.plugin.binaryCompatibilityValidator)

    implementation(libs.testcontainers.postgres)
    implementation(libs.testcontainers.core)
    implementation(libs.db.postgres)
//    implementation("com.github.docker-java:docker-java-core:3.3.6")
}
