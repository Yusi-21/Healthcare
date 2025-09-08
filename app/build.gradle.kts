plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mirea.healthcare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mirea.healthcare"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.core:core-splashscreen:1.0.1") // SplashScreen API
    implementation("androidx.room:room-runtime:2.4.0")

    annotationProcessor("androidx.room:room-runtime:2.4.0")
    annotationProcessor("androidx.room:room-compiler:2.4.0")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.biometric:biometric:1.1.0")

    implementation("androidx.work:work-runtime:2.7.1")

    implementation("com.airbnb.android:lottie:6.3.0")

}