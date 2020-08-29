apply plugin: "kotlin-multiplatform"

kotlin {
    jvm {
        compilations.main.kotlinOptions {
            jvmTarget = "1.8"
        }
        compilations.test.kotlinOptions {
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
    // Configure all compilations of all targets:
    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }
}
