import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("org.catrobat.gradle.androidemulators")
}

extra["CIBuild"] = System.getenv("CI") == "true"
// allows for -Dpre-dex=false to be set
extra["preDexEnabled"] = System.getProperty("pre-dex", "true") == "true"

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.github.felipehjcosta.chatapp"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "CHAT_URL_SERVER", "\"ws://10.0.2.2:8089\"")
        }
        getByName("release") {
            buildConfigField("String", "CHAT_URL_SERVER", "\"ws://10.0.2.2:8089\"")
            isMinifyEnabled = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
            )
        }
    }

    viewBinding {
        isEnabled = true
    }

    dexOptions {
        // Skip pre-dexing when running on CI or when disabled via -Dpre-dex=false.
        val preDexEnabled = extra["preDexEnabled"] as Boolean
        val CIBuild = extra["preDexEnabled"] as Boolean

        preDexLibraries = preDexEnabled && !CIBuild
    }

    dexOptions {

    }

    packagingOptions {
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }

    sourceSets.getByName("main").java.srcDir("src/main/kotlin")
    sourceSets.getByName("test").java.srcDir("src/test/kotlin")
    sourceSets.getByName("debug").java.srcDir("src/debug/kotlin")
    sourceSets.getByName("androidTest").java.srcDir("src/androidTest/kotlin")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = "1.8"
        }
    }
}

emulators {
    install(project.hasProperty("installSdk"))

    dependencies(delegateClosureOf<org.catrobat.gradle.androidemulators.DependenciesExtension> {
        sdk()
    })

    emulator("pixel_2_android29", delegateClosureOf<org.catrobat.gradle.androidemulators.EmulatorExtension> {

        avd(delegateClosureOf<org.catrobat.gradle.androidemulators.AvdSettings> {
            systemImage = "system-images;android-29;default;x86_64"
            sdcardSizeMb = 512
            hardwareProperties = mapOf("hw.ramSize" to 1536, "vm.heapSize" to 256, "disk.dataPartition.size" to "1024MB")
            screenDensity = "xxhdpi"

            parameters(delegateClosureOf<org.catrobat.gradle.androidemulators.EmulatorStarter> {
                resolution = "1080x1920"
                language = "en"
                country = "US"
            })
        })
    })
}

val kotlin_version: String by project
val android_nav_version: String by project
val android_jetpack_lifecycle: String by project
val junit_version: String by project


dependencies {
    implementation(project(":common:client"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation("com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:1.0.0")
    implementation("com.github.felipehjcosta:recyclerview-dsl:0.8.0")

    implementation("androidx.navigation:navigation-fragment-ktx:$android_nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$android_nav_version")

    implementation("androidx.lifecycle:lifecycle-runtime:$android_jetpack_lifecycle")
    implementation("androidx.lifecycle:lifecycle-common-java8:$android_jetpack_lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$android_jetpack_lifecycle")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$android_jetpack_lifecycle")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:$junit_version")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
