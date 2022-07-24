import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    kotlin("multiplatform") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.shadow)
}

buildscript {
    dependencies {
        classpath(libs.square.kotlinPoet)
    }
}

group = "cz.drekorian.avonmobilefetcher"
version = "2.1.0"

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
                    getByName("${targetName}Jar") {
                        finalizedBy(shadowJar)
                    }
                }
            }
        }
    }

    mingwX64("native").apply {
        compilations.forEach { compilation ->
            compilation.kotlinOptions.freeCompilerArgs = mutableListOf(
                "-linker-options", "-L${System.getenv("MINGWX64_HOME")}\\lib"
            )
        }

        binaries {
            executable {
                val appendix = when (buildType) {
                    NativeBuildType.DEBUG -> "-debug"
                    else -> ""
                }

                baseName = "${rootProject.name}$appendix-$version"
                entryPoint = "${project.group}.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.logging)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.curl)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlin.logging.jvm)
                implementation(libs.kotlinx.coroutines.sl4j)
                implementation(libs.ktor.client.cio)
                implementation(libs.sl4j.simple)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val nativeTest by getting
    }
}
