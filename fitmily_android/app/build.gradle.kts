plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.ssafy.fitmily_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ssafy.fitmily_android"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // compose
    implementation("androidx.compose.foundation:foundation:1.9.0-alpha01")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")

    // 네이버 지도
    implementation("com.naver.maps:map-sdk:3.21.0")

    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.56.2")

    // ksp
    ksp("com.google.dagger:hilt-compiler:2.56.2")

    // hilt compose
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // preference data store
    implementation("androidx.datastore:datastore-preferences:1.1.6")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.0")

    // kizitonwose
    implementation("com.kizitonwose.calendar:compose:2.6.2")

    // Datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    // WheelPicker
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")
}