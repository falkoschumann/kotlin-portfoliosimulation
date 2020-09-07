plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.0" apply false
}

allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    version = "1.0.0"
}
