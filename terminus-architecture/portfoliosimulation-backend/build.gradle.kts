plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":portfoliosimulation-contract"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.glassfish:javax.json:1.1.4")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
