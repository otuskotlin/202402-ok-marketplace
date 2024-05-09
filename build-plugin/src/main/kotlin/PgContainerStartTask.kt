package ru.otus.otuskotlin.marketplace.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.work.InputChanges

open class PgContainerStartTask : DefaultTask() {
    override fun getGroup(): String = "containers"

//    var port: Int = 5432
    var pgUrl: String = ""

    @TaskAction
    fun execute(inputs: InputChanges) { // InputChanges parameter
        val msg = if (inputs.isIncremental) "CHANGED inputs are out of date"
        else "ALL inputs are out of date"
        println(msg)
    }
}
