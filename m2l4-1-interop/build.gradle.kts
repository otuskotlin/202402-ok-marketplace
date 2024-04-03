plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser {
            testTask {
//                useKarma {
//                    // Выбираем браузеры, на которых будет тестироваться
//                    useChrome()
//                    useFirefox()
//                }
                // Без этой настройки длительные тесты не отрабатывают
                useMocha {
                    timeout = "100s"
                }
            }

        }
        binaries.library()
        generateTypeScriptDefinitions()
    }
    listOf(
        linuxX64(),
        macosArm64(),
    ).forEach {
        it.apply {
            compilations.getByName("main") {
                cinterops {
                    // настраиваем cinterop в файле src/nativeInterop/cinterop/libcurl.def
                    val libcurl by creating
//                    create("libcurl")
                }
            }
            binaries {
                executable {
                    entryPoint = "main"
                }
            }
        }
    }
    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    // Description of modules corresponding to our target platforms
    //  common - common code that we can use on different platforms
    //  for each target platform, we can specify our own specific dependencies
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        // dependencies from npm
        val jsMain by getting {
            dependencies {
                implementation(npm("js-big-decimal", "~1.3.4"))
                implementation(npm("is-sorted", "~1.0.5"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        // С 1.9.20 можно так
        nativeMain {
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

    }
}
