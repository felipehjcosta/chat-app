apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: "androidx.navigation.safeargs.kotlin"

ext {
    CIBuild = System.getenv("CI") == "true"
    // allows for -Dpre-dex=false to be set
    preDexEnabled = "true".equals(System.getProperty("pre-dex", "true"))
}

android {
    compileSdkVersion 29

    defaultConfig {
        applicationId "com.github.felipehjcosta.chatapp"
        minSdkVersion 21
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        debug {
            buildConfigField 'String', 'CHAT_URL_SERVER', "\"ws://${getIP()}:8089\""
        }
        release {
            buildConfigField 'String', 'CHAT_URL_SERVER', "\"ws://${getIP()}:8089\""
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    viewBinding {
        enabled = true
    }

    dexOptions {
        // Skip pre-dexing when running on CI or when disabled via -Dpre-dex=false.
        preDexLibraries = preDexEnabled && !CIBuild
    }

    packagingOptions {
        exclude 'META-INF/kotlinx-serialization-runtime.kotlin_module'
    }

    sourceSets {
        androidTest.java.srcDirs += "src/androidTest/kotlin"
        debug.java.srcDirs += "src/debug/kotlin"
        main.java.srcDirs += "src/main/kotlin"
        test.java.srcDirs += "src/test/kotlin"
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
    kotlinOptions {
        allWarningsAsErrors = true
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation project(":common:client")
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation "androidx.appcompat:appcompat:1.1.0"
    implementation "androidx.recyclerview:recyclerview:1.1.0"
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:1.0.0'
    implementation "com.github.felipehjcosta:recyclerview-dsl:0.8.0"

    implementation "androidx.navigation:navigation-fragment-ktx:$android_nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$android_nav_version"

    implementation "androidx.lifecycle:lifecycle-runtime:$android_jetpack_lifecycle"
    implementation "androidx.lifecycle:lifecycle-common-java8:$android_jetpack_lifecycle"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$android_jetpack_lifecycle"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$android_jetpack_lifecycle"

    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation "junit:junit:$junit_version"

    androidTestImplementation 'androidx.test:runner:1.2.0'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
}
