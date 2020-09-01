import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
}

val serialization_version: String by project

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser()
    }

    iOSTarget("ios")

    watchOSTarget("watch")

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
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
