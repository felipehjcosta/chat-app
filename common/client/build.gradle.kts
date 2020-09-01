import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

version = "0.1.0"
val ios_framework_name = "Client"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
    iOSTarget("ios") {
        binaries {
            framework(ios_framework_name)
        }
    }

    watchOSTarget("watch") {
        binaries {
            framework(ios_framework_name)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:client-websocket"))
                api(project(":common:core"))
                api(project(":common:logger"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
    // Configure all compilations of all targets:
    targets.all {
        compilations.all {
            kotlinOptions.allWarningsAsErrors = true
        }
    }
}

val packIOSForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
            .getByName<KotlinNativeTarget>("ios")
            .binaries.getFramework(ios_framework_name, mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n"
                + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                + "cd '${rootProject.rootDir}'\n"
                + "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}
tasks.getByName("build").dependsOn(packIOSForXcode)

val packWatchForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
            .getByName<KotlinNativeTarget>("watch")
            .binaries.getFramework(ios_framework_name, mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n"
                + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                + "cd '${rootProject.rootDir}'\n"
                + "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}
tasks.getByName("build").dependsOn(packWatchForXcode)

val packForXcode by tasks.creating(Sync::class) {
    dependsOn(packIOSForXcode, packWatchForXcode)
}

fun KotlinTargetContainerWithPresetFunctions.iOSTarget(name: String, block: KotlinNativeTarget.() -> Unit = {}): KotlinNativeTarget {
    return if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
        iosArm64(name, block)
    else
        iosX64(name, block)
}

fun KotlinTargetContainerWithPresetFunctions.watchOSTarget(name: String, block: KotlinNativeTarget.() -> Unit = {}): KotlinNativeTarget {
    return if (System.getenv("SDK_NAME")?.startsWith("watchos") == true)
        watchosArm64(name, block)
    else
        watchosX86(name, block)
}