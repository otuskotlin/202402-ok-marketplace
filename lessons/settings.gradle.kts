pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "lessons"

include("m1l1-first")
include("m1l2-basic")
include("m1l3-func")
include("m1l4-oop")
include("m1l5-dsl")
include("m2l1-coroutines")
include("m2l2-flows")
include("m2l3-kmp")
include("m2l4-1-interop")
include("m2l4-2-jni")
include("m2l5-gradle")

include(":m2l5-gradle:sub1:ssub1", ":m2l5-gradle:sub1:ssub2")

include(":m2l5-gradle-sub2")
project(":m2l5-gradle-sub2").apply {
    projectDir = file("m2l5-gradle/sub2")
    name = "m2l5-custom-sub2"
}

// Включает вот такую конструкцию
//implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
