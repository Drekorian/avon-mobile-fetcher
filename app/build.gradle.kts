@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.allopen)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.shadow)
}

group = "cz.drekorian.avonmobilefetcher"
version = "3.0.0"

repositories {
    mavenCentral()
}

private val testingTaskRegex = """^.*Tests?$""".toRegex()

val isTesting = gradle
    .startParameter
    .taskNames
    .any { task -> task.matches(testingTaskRegex) }

if (isTesting) {
    allOpen {
        annotation("cz.drekorian.avonmobilefetcher.OpenForTesting")
    }
}

mokkery {
    ignoreFinalMembers = true
    stubs.allowConcreteClassInstantiation = true
}

buildConfig {
    packageName(group.toString())
    buildConfigField("String", "appVersion", provider { """"$version"""" })
    useKotlinOutput()
}

kotlin {
    jvm {
        mainRun {
            mainClass = "${project.group}.MainKt"
        }
    }


    tasks.named<ShadowJar>("shadowJar") {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("${project.version}")
        archiveAppendix.set("all")

        manifest {
            attributes("Main-Class" to "${project.group}.MainKt")
        }
        mergeServiceFiles()
    }

    macosArm64 {
        binaries {
            executable {
                baseName = buildString {
                    append(rootProject.name)
                    append("-macos-arm64")
                    if (buildType == NativeBuildType.DEBUG) {
                        append("-debug")
                    }
                    append("-$version")
                }
                entryPoint = "${project.group}.main"
            }
        }
    }

    mingwX64 {
        binaries {
            executable {
                baseName = buildString {
                    append(rootProject.name)
                    append("-mingwx64")
                    if (buildType == NativeBuildType.DEBUG) {
                        append("-debug")
                    }
                    append("-$version")
                }
                entryPoint = "${project.group}.main"
            }
        }
    }

    sourceSets {
        commonMain.get().dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.clikt)
            implementation(libs.koin.core)
            implementation(libs.kotlin.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.get().dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmMain.get().dependencies {
            implementation(libs.kotlin.logging.jvm)
            implementation(libs.kotlinx.coroutines.sl4j)
            implementation(libs.ktor.client.cio)
            implementation(libs.sl4j.simple)
        }

        jvmTest.get().dependencies {
            implementation(libs.kotlin.reflect)
        }

        macosMain.get().dependencies {
            implementation(libs.ktor.client.darwin)
        }

        mingwMain.get().dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }
}
