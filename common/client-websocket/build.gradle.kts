import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

val coroutines_version: String by project
val junit_version: String by project

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

    iOSTarget("ios")

    watchOSTarget("watch")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:logger"))
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.squareup.okhttp3:okhttp:4.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("junit:junit:$junit_version")
                implementation("io.mockk:mockk:1.10.3")
                implementation("com.squareup.okhttp3:mockwebserver:4.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation(npm("mock-socket", "^9.0.0"))
            }
        }

        val appleMain by creating {
            dependsOn(commonMain)
        }

        val appleTest by creating {
            dependsOn(commonTest)
        }

        val iosMain by getting {
            dependsOn(appleMain)
        }

        val iosTest by getting {
            dependsOn(appleTest)
        }

        val watchMain by getting {
            dependsOn(appleMain)
        }

        val watchTest by getting {
            dependsOn(appleTest)
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