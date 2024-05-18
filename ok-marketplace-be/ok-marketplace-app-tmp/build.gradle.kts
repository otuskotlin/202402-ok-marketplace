plugins {
    id("build-jvm")
    application
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.app.tmp.MainKt")
}

dependencies {
    implementation(project(":ok-marketplace-api-log1"))
    implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-common")
    implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-logback")

    implementation(project(":ok-marketplace-common"))

    implementation(libs.coroutines.core)
}
