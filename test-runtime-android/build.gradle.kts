plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "wtf.emulator"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":observer-interface"))
    implementation("junit:junit:4.13.2")
    //noinspection GradleDependency - we don't want to force downstream upgrades
    implementation("androidx.test:monitor:1.1.0")
}
