plugins {
    id("build-jvm")
}

group = "ru.otus.otuskotlin.marketplace.ml"
version = "0.0.1"

dependencies {
    implementation(libs.ml.onnx.runtime)
    implementation(libs.ml.tokenizer)
    implementation(libs.logback)

    testImplementation(kotlin("test-junit5"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}
