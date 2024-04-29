plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":ok-marketplace-common"))
                implementation(project(":ok-marketplace-repo-common"))

                implementation(libs.coroutines.core)
                implementation(libs.db.cache4k)
                implementation(libs.uuid)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":ok-marketplace-repo-tests"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
