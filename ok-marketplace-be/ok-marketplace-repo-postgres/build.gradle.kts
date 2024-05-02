plugins {
    id("build-kmp")
//    id("app.cash.sqldelight") version "2.0.2"
//    alias(libs.plugins.kotlinx.serialization)
}
repositories {
    google()
    mavenCentral()
}

//sqldelight {
//    databases {
//        create("JPg") {
//            packageName.set("com.example")
//            dialect("app.cash.sqldelight:postgresql-dialect:2.0.2")
//            srcDirs.setFrom("src/sqldelight")
//            schemaOutputDirectory.set(layout.buildDirectory.dir("xxx"))
//            deriveSchemaFromMigrations.set(true)
//        }
////        create("NPg") {
////            packageName.set("com.example")
////            dialect("app.softwork:postgres-native-sqldelight-dialect:0.0.11")
////            srcDirs.setFrom("src/sqldelight")
////            deriveSchemaFromMigrations.set(true)
////        }
//    }
////    linkSqlite.set(false)
//}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.okMarketplaceCommon)
                api(projects.okMarketplaceRepoCommon)

                implementation(libs.coroutines.core)
                implementation(libs.uuid)
                implementation("io.github.moreirasantos:pgkn:1.1.0")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.okMarketplaceRepoTests)
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
//                implementation("app.cash.sqldelight:postgres-driver:2.0.2")
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        nativeMain {
            dependencies {
                implementation(kotlin("stdlib"))
//                implementation("app.softwork:postgres-native-sqldelight-driver:0.0.11")
            }
        }
    }
}
