plugins {
    id("build-jvm")
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(projects.okMarketplaceCommon)
    implementation(projects.okMarketplaceRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.jdk9)
    implementation(libs.uuid)
    implementation(libs.bundles.cassandra)
    kapt(libs.db.cassandra.kapt)

//    implementation("com.datastax.oss:java-driver-core:$cassandraDriverVersion")
//    implementation("com.datastax.oss:java-driver-query-builder:$cassandraDriverVersion")
//    kapt("com.datastax.oss:java-driver-mapper-processor:$cassandraDriverVersion")
//    implementation("com.datastax.oss:java-driver-mapper-runtime:$cassandraDriverVersion")
//
    testImplementation(projects.okMarketplaceRepoTests)
//    testImplementation("org.testcontainers:cassandra:$testContainersVersion")
    testImplementation(libs.testcontainers.cassandra)
}
