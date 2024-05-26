package ru.otus.otuskotlin.marketplace.plugin

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

@Suppress("unused")
internal class BuildPluginJvm : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        plugins.withId("org.jetbrains.kotlin.jvm") {
            extensions.configure<KotlinJvmProjectExtension> {
                val libs = project.the<LibrariesForLibs>()
                sourceSets.configureEach {
                    languageSettings.apply {
                        languageVersion = libs.versions.kotlin.language.get()
                        progressiveMode = true
                        optIn("kotlin.time.ExperimentalTime")
                    }
                }
                jvmToolchain {
                    languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.language.get()))
                }
                compilerOptions {
                    jvmTarget.set(JvmTarget.valueOf("JVM_${libs.versions.jvm.compiler.get()}"))
                }
            }
        }
        group = rootProject.group
        version = rootProject.version
    }
}
