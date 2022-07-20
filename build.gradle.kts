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
                implementation("io.github.microutils:kotlin-logging:2.1.23")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.3")
                implementation("io.ktor:ktor-client-core:2.0.3")
                implementation("io.ktor:ktor-client-logging:2.0.3")
                implementation("io.ktor:ktor-client-serialization:2.0.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-curl:2.0.3")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
                implementation("io.ktor:ktor-client-cio:2.0.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.3")
                implementation("org.slf4j:slf4j-simple:1.7.36")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val nativeTest by getting
    }
}
