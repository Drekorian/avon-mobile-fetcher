import org.gradle.jvm.tasks.Jar
import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "cz.drekorian.avonmobilefetcher"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = URI("https://jitpack.io")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("com.github.jkcclemens", "khttp", "-SNAPSHOT")
    compile("io.github.microutils:kotlin-logging:1.6.22")
    compile("org.slf4j", "slf4j-simple", "1.7.25")
    compile("org.jsoup", "jsoup", "1.11.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("com.natpryce:hamkrest:1.7.0.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    manifest {
        attributes["Main-Class"] = "cz.drekorian.avonfetcher.Main"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    archiveName = "avon-mobile-fetcher.jar"
    manifest.attributes["Main-Class"] = "cz.drekorian.avonmobilefetcher.Main"
    from(configurations.runtime.files.map { if (it.isDirectory) it else zipTree(it) })
}
