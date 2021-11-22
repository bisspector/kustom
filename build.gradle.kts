plugins {
    java
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
}

group = "me.bisspector"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    implementation("org.slf4j", "slf4j-api", "1.7.30")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("ch.qos.logback:logback-core:1.3.0-alpha5")

    implementation("io.netty:netty-all:4.1.60.Final")

    implementation("io.netty:netty-transport-native-epoll:4.1.60.Final")
    implementation("io.netty:netty-transport-native-kqueue:4.1.60.Final")

    implementation("net.kyori:adventure-api:4.7.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.6.0")

    testImplementation("junit", "junit", "4.12")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
