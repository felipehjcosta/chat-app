import io.gitlab.arturbosch.detekt.detekt

buildscript {

    val kotlin_version: String by project
    val android_gradle_plugin: String by project
    val detekt_gradle_plugin: String by project
    val android_nav_version: String by project
    val android_emulator_version: String by project

    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
        maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "http://repository.openbakery.org/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath("com.android.tools.build:gradle:$android_gradle_plugin")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detekt_gradle_plugin")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$android_nav_version")
        classpath("org.catrobat.gradle.androidemulators:android-emulators-gradle:$android_emulator_version")
        classpath("com.novoda:gradle-android-command-plugin:2.1.0")
        classpath("gradle.plugin.org.openbakery:plugin:0.20.0")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "http://dl.bintray.com/kotlin/kotlinx.html")
        maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers")
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config = files("${project.rootDir}/default-detekt-config.yml")
    }

    tasks {
        withType<io.gitlab.arturbosch.detekt.Detekt> {
            exclude("**/index.module_hoist-non-react-statics.kt")
            exclude("**/index.module_tiny-invariant.kt")
            exclude("**/index.module_tiny-warning.kt")
        }
    }
}

tasks {
    register("runBackend") {
        dependsOn(":backend:run")
    }

    register("runFrontend") {
        dependsOn(":frontend:browserDevelopmentRun")
    }

    register("runAndroid") {
        val startEmulatorTask = project(":android").tasks["startEmulator"]
        val runDebugTask = project(":android").tasks["runDebug"]
        dependsOn(startEmulatorTask)
        dependsOn(runDebugTask)
        runDebugTask.mustRunAfter(startEmulatorTask)
    }

    register("runIOS") {
        val packForXcodeTask = project(":common:client").tasks["packForXcode"]
        val simulatorRunAppTask = project(":ios").tasks["simulatorRunApp"]
        dependsOn(packForXcodeTask)
        dependsOn(simulatorRunAppTask)
        simulatorRunAppTask.mustRunAfter(packForXcodeTask)
    }
}