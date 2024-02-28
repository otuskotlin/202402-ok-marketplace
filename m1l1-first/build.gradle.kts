plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
