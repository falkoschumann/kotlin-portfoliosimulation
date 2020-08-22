plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.openjfx.javafxplugin") version "0.0.8"
}

dependencies {
    api(project(":portfoliosimulation-contract"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

javafx {
    version = "14"
    modules("javafx.controls", "javafx.fxml")
}
