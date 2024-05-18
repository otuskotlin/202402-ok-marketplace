plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":ok-marketplace-lib-logging-common"))
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.logback)

    implementation(libs.logback.logstash)
    api(libs.logback.appenders)
    api(libs.logger.fluentd)

    testImplementation(kotlin("test-junit"))
}
