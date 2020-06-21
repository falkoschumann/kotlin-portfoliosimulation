plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

dependencies {
    implementation(project(":portfoliosimulation-backend"))
    implementation(project(":portfoliosimulation-frontend-fx"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "de.muspellheim.portfoliosimulation.fx.AppFxKt"
}

javafx {
    version = "14"
    modules("javafx.controls")
}
