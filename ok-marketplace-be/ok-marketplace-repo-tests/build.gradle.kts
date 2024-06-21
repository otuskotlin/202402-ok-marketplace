plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(projects.okMarketplaceCommon)
                implementation(projects.okMarketplaceRepoCommon)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.okMarketplaceStubs)
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}
