package ru.otus.otuskotlin.marketplace.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class BuildPluginJvm : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        group = rootProject.group
        version = rootProject.version
    }
}
