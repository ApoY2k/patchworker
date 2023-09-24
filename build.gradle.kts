plugins {
    kotlin("jvm") version "1.9.20-Beta2"
    application
}

group = "apoy2k"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("apoy2k.patchworker.MainKt")
}
