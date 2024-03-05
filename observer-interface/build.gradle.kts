plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "wtf.emulator.observer.itf"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    buildFeatures {
        aidl = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api("com.google.auto.value:auto-value-annotations:1.10.4")
    //noinspection GradleDependency - we don't want to force downstream upgrades
    api("androidx.annotation:annotation:1.0.0")
    annotationProcessor("com.google.auto.value:auto-value:1.10.4")
}