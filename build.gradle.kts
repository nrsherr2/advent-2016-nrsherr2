plugins {
    kotlin("jvm") version "1.6.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/main")
        }
    }
    wrapper {
        gradleVersion = "7.3"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}
