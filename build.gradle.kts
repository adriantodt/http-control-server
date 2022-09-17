import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.javalin:javalin:3.8.0")
    implementation("org.kodein.di:kodein-di-generic-jvm:6.5.5")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}