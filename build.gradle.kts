plugins {
    kotlin("jvm") version "1.3.72"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClassName = "MainKt"
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}