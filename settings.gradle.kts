pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "ok-marketplace-202402"

//includeBuild("lessons")
includeBuild("ok-marketplace-be")
includeBuild("ok-marketplace-states")
includeBuild("ok-marketplace-libs")

includeBuild("ok-marketplace-tests")
includeBuild("pgkn")
