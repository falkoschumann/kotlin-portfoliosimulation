plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    `java-library`
}

dependencies {
    api(project(":portfoliosimulation-contract"))
    implementation("org.glassfish:javax.json:1.1.4")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
