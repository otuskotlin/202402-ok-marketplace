import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

tasks {
    // Таска для создания файла
    val myCustomTask by creating {
        group = "my group"
        val dir = layout.buildDirectory.dir("my-in")
        outputs.dir(dir)

        doFirst {
            val fileContent = """
                package my.package
                
                const val MY_VERSION: String = "${project.version}"
            """.trimIndent()
            dir
                .get()
                .file("my-version.kt")
                .asFile
                .apply {
                    ensureParentDirsCreated()
                    writeText(fileContent)
                }
        }
    }

    val myCopyTask by creating(Copy::class) {
        dependsOn(myCustomTask)

        group = "my group"
        from(layout.buildDirectory.dir("my-in"))
        into(layout.buildDirectory.dir("tmp"))
    }

    compileKotlin {
        dependsOn(myCopyTask)
    }
}

tasks {
    create("myTask") {
        println("Configuration stage")
        doFirst { println("At task starts") }
        doLast { println("At task ends") }
    }
}

afterEvaluate {
    tasks {
        create("myOtherTask") {
            println("After other tasks initialized")
        }

        forEach { println("TASK $it") }
    }
}

