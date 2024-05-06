package ru.otus.otuskotlin.marketplace.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.testcontainers.containers.PostgreSQLContainer

@Suppress("unused")
internal class BuildPluginPgContainer : Plugin<Project> {

//    private val pgContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
//        withUsername(pgUsername)
//        withPassword(pgPassword)
//        withDatabaseName(pgDbName)
//        this.startupCheckStrategy
////    waitingFor(Wait.forLogMessage("database system is ready to accept connections", 1))
//    }


    override fun apply(project: Project): Unit = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        tasks.register("pgStart") {
            group = "containers"
        }
        tasks.register("pgStop") {
            group = "containers"
        }
    }
}
