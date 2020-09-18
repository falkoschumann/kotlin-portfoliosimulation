plugins {
    id("org.jetbrains.kotlin.jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

dependencies {
    implementation(project(":portfoliosimulation-backend"))
    implementation(project(":portfoliosimulation-frontend-fx"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
}

application {
    mainClassName = "de.muspellheim.portfoliosimulation.fx.AppFxKt"
}

javafx {
    version = "14"
    modules("javafx.controls")
}
