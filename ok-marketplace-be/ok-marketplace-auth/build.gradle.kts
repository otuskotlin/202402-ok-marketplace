plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val datetimeVersion: String by project
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api(libs.coroutines.core)
                api(libs.kotlinx.datetime)
                implementation(projects.okMarketplaceCommon)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
