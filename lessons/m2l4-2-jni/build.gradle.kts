import org.gradle.internal.jvm.Jvm
import java.io.ByteArrayOutputStream

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    // Description of modules corresponding to our target platforms
    //  common - common code that we can use on different platforms
    //  for each target platform, we can specify our own specific dependencies
    sourceSets {
        val jvmMain by getting {
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks {
    val pathToLib: String = project.layout.buildDirectory.dir("c/lib").get().toString()
    println("PATH TO LIB: $pathToLib")
    withType<Test>().all {
        systemProperty("java.library.path", pathToLib)
    }

    val jniHeaderDirectory = layout.buildDirectory.dir("jniIncludes").get().asFile

    val generateJniHeaders by creating {
        group = "build"
        dependsOn(getByName("compileKotlinJvm"))

        // For caching
        inputs.dir("src/jvmMain/kotlin")
        outputs.dir("src/jvmMain/generated/jni")

        doLast {
            val javaHome = Jvm.current().javaHome
            val javap = javaHome.resolve("bin").walk().firstOrNull { it.name.startsWith("javap") }?.absolutePath ?: error("javap not found")
            val javac = javaHome.resolve("bin").walk().firstOrNull { it.name.startsWith("javac") }?.absolutePath ?: error("javac not found")
            val buildDir = file("build/classes/kotlin/jvm/main")
            val tmpDir = file("build/tmp/jvmJni").apply { mkdirs() }

            val bodyExtractingRegex = """^.+\Rpublic \w* ?class ([^\s]+).*\{\R((?s:.+))\}\R$""".toRegex()
            val nativeMethodExtractingRegex = """.*\bnative\b.*""".toRegex()

            buildDir.walkTopDown()
                .filter { "META" !in it.absolutePath }
                .forEach { file ->
                    if (!file.isFile) return@forEach
                    println("FILE: $file")

                    val output = ByteArrayOutputStream().use {
                        project.exec {
                            commandLine(javap, "-private", "-cp", buildDir.absolutePath, file.absolutePath)
                            standardOutput = it
                        }.assertNormalExitValue()
                        it.toString()
                    }

                    val (qualifiedName, methodInfo) = bodyExtractingRegex.find(output)?.destructured ?: return@forEach

                    val lastDot = qualifiedName.lastIndexOf('.').takeIf { it >= 0 } ?: return@forEach
                    val packageName = qualifiedName.substring(0, lastDot)
                    val className = qualifiedName.substring(lastDot+1, qualifiedName.length)

                    val nativeMethods = nativeMethodExtractingRegex
                        .findAll(methodInfo)
                        .map { it.groups }
                        .flatMap { it.asSequence().mapNotNull { group -> group?.value } }
                        .toList()

                    if (nativeMethods.isEmpty()) return@forEach

                    val source = buildString {
                        appendLine("package $packageName;")
                        appendLine("public class $className {")
                        for (method in nativeMethods) {
                            if ("()" in method) appendLine(method)
                            else {
                                val updatedMethod = StringBuilder(method).apply {
                                    var count = 0
                                    var i = 0
                                    while (i < length) {
                                        if (this[i] == ',' || this[i] == ')') insert(i, " arg${count++}".also { i += it.length + 1 })
                                        else i++
                                    }
                                }
                                appendLine(updatedMethod)
                            }
                        }
                        appendLine("}")
                    }
                    val outputFile = tmpDir.resolve(packageName.replace(".", "/")).apply { mkdirs() }.resolve("$className.java").apply { delete() }.apply { createNewFile() }
                    outputFile.writeText(source)

                    project.exec {
                        commandLine(javac, "-h", jniHeaderDirectory.absolutePath, outputFile.absolutePath)
                    }.assertNormalExitValue()
                }
        }
    }
}
