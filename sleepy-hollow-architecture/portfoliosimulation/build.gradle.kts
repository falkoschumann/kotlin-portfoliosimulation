plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    application
}

dependencies {
    implementation(project(":portfoliosimulation-backend"))
    implementation(project(":portfoliosimulation-frontend"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "de.muspellheim.portfoliosimulation.AppKt"
}
