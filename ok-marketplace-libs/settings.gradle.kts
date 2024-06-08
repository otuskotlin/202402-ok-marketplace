rootProject.name = "ok-marketplace-libs"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

include(":ok-marketplace-lib-logging-common")
include(":ok-marketplace-lib-logging-kermit")
include(":ok-marketplace-lib-logging-logback")
include(":ok-marketplace-lib-logging-socket")

include(":ok-marketplace-lib-cor")
