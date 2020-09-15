plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":portfoliosimulation-backend"))
    implementation(project(":portfoliosimulation-frontend"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
