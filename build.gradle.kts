import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    java
    kotlin("jvm") version "1.6.10"
}

group = "cz.drekorian.avonmobilefetcher"
version = "1.7.0"

repositories {
    mavenCentral()
    maven {
        url = URI("https://jitpack.io")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("com.github.jkcclemens", "khttp", "-SNAPSHOT")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("org.slf4j", "slf4j-simple", "1.7.25")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("com.natpryce:hamkrest:1.8.0.1")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    archiveBaseName.set("avon-mobile-fetcher")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes("Main-Class" to "cz.drekorian.avonmobilefetcher.Main") }
    from(
        configurations.runtimeClasspath.get().map { file ->
            when {
                file.isDirectory -> file
                else -> zipTree(file)
            }
        } + sourceSets.main.get().output
    )
}
