apply plugin: "kotlin-multiplatform"

kotlin {
    jvm {
        compilations.main.kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    js {
        browser()
    }
    targets {
        final def iOSTarget = System.getenv('SDK_NAME')?.startsWith("iphoneos") ? presets.iosArm64 : presets.iosX64

        fromPreset(iOSTarget, 'ios')

        final def watchTarget = System.getenv('SDK_NAME')?.startsWith("watchos") ? presets.watchosArm64 : presets.watchosX86

        fromPreset(watchTarget, 'watch')
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation project(":common:core")
                implementation project(":common:logger")
                implementation kotlin('stdlib-common')
                implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
            }
        }

        commonTest {
            dependencies {
                implementation kotlin('test-common')
                implementation kotlin('test-annotations-common')
            }
        }

        jvmMain {
            dependencies {
                implementation 'com.squareup.okhttp3:okhttp:4.3.0'
                implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
            }
        }

        jvmTest {
            dependencies {
                implementation kotlin('test')
                implementation kotlin('test-junit')
                implementation "junit:junit:$junit_version"
                implementation "io.mockk:mockk:1.9"
                implementation 'com.squareup.okhttp3:mockwebserver:4.3.0'
            }
        }

        jsTest {
            dependencies {
                implementation kotlin('test-js')
                implementation npm("mock-socket", "^9.0.0")
            }
        }

        appleMain {
            dependsOn commonMain
        }

        appleTest {
            dependsOn commonTest
        }

        iosMain {
            dependsOn appleMain
        }

        iosTest {
            dependsOn appleTest
        }

        watchMain {
            dependsOn appleMain
        }

        watchTest {
            dependsOn appleTest
        }
    }
    // Configure all compilations of all targets:
    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }
}
