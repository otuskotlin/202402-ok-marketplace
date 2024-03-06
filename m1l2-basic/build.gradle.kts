plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("ru.otus.otuskotlin.m1l2.MainKt")
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test-junit"))
}
