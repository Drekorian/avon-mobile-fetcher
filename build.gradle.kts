import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
}

group = "cz.drekorian.avonmobilefetcher"
version = "3.0.0"

repositories {
    mavenCentral()
}

buildConfig {
    packageName(group.toString())
    buildConfigField("String", "appVersion", provider { """"$version"""" })
    useKotlinOutput()
}

kotlin {
    jvm {
        compilations.named("main") {
            tasks {
                register<ShadowJar>("shadowJarJvm") {
                    group = "build"
                    from(output)
                    configurations = listOf(runtimeDependencyFiles)

                    archiveBaseName.set(rootProject.name)
                    archiveClassifier.set("")
                    archiveVersion.set("${project.version}")
                    archiveAppendix.set("all")

                    manifest {
                        attributes("Main-Class" to "${project.group}.MainKt")
                    }
                    mergeServiceFiles()
                }.also { shadowJar ->
                    getByName("${this@jvm.targetName}Jar") {
                        finalizedBy(shadowJar)
                    }
                }
            }
        }
    }

    mingwX64 {
        binaries {
            executable {
                baseName = buildString {
                    append(rootProject.name)
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
            implementation(libs.clikt)
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

        mingwMain.get().dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }
}
