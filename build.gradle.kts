import org.gradle.jvm.tasks.Jar

plugins {
    java
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "cz.drekorian.avonmobilefetcher"
version = "2.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        tasks.withType<Jar>().forEach { jarTask -> jarTask.enabled = false }
        compilations {
            val main = getByName("main")
            tasks {
                register<Copy>("unzip") {
                    group = "build"
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    val targetDir = File(buildDir, "3rd-libs")
                    project.delete(files(targetDir))
                    main.compileDependencyFiles.forEach {
                        from(zipTree(it))
                        into(targetDir)
                    }
                }
                register<Jar>("fatJar") {
                    group = "library"
                    manifest {
                        attributes("Main-Class" to "cz.drekorian.avonmobilefetcher.MainKt")
                    }
                    archiveBaseName.set("${project.name}-fat")
                    val thirdLibsDir = File(buildDir, "3rd-libs")
                    from(main.output.classesDirs, thirdLibsDir)
                    with(jar.get() as CopySpec)
                }
            }
            tasks.getByName("fatJar").dependsOn("unzip")
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
                baseName = "avon-mobile-fetcher-$version"
                entryPoint = "cz.drekorian.avonmobilefetcher.main"
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
