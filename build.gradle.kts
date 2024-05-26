plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.otus.otuskotlin.marketplace"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    create("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    val buildImages: Task by creating {
        dependsOn(gradle.includedBuild("ok-marketplace-be").task(":buildImages"))
    }
    val e2eTests: Task by creating {
        dependsOn(gradle.includedBuild("ok-marketplace-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    create("check") {
        group = "verification"
        dependsOn(buildImages)
        dependsOn(e2eTests)
    }
}
