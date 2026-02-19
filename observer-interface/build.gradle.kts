plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "wtf.emulator.observer.itf"
    compileSdk = 36

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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


dependencies {
    api(libs.autovalue.annotations)
    api(fixed.androidx.annotation)
    annotationProcessor(libs.autovalue.processor)
}
