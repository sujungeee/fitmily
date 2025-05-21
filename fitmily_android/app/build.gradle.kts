import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.gms.google.services)
}

val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))
val naverClientId: String = properties.getProperty("NAVER_CLIENT_ID")
val weatherApiKey: String = properties.getProperty("WEATHER_API_KEY")
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

        buildConfigField("String", "NAVER_CLIENT_ID", naverClientId)
        manifestPlaceholders["NAVER_CLIENT_ID"] =  naverClientId

        buildConfigField("String", "WEATHER_API_KEY", weatherApiKey)
        manifestPlaceholders["WEATHER_API_KEY"] = weatherApiKey
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        dataBinding = true
        buildConfig = true
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
    implementation(libs.firebase.messaging.ktx)
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

    // krossbow websocket
    implementation(libs.krossbow.stomp.core)
    implementation(libs.krossbow.websocket.okhttp)
    implementation(libs.krossbow.stomp.moshi)

    // krossbow moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // ted image picker
    implementation("io.github.ParkSangGwon:tedimagepicker:1.6.1")

    // coil3
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    // kizitonwose
    implementation("com.kizitonwose.calendar:compose:2.6.2")

    // Datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    // WheelPicker
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")


    // 네이버 지도
    implementation("com.naver.maps:map-sdk:3.21.0")
    implementation("io.github.fornewid:naver-map-compose:1.5.7")

    // 위치 서비스
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("io.github.fornewid:naver-map-location:21.0.2")

    // stomp
    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")

    //RxJava
    implementation ("io.reactivex.rxjava2:rxjava:2.2.5")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // paging
    implementation(libs.paging.compose)
    implementation(libs.paging.common.ktx)

    // coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-gif:2.4.0")
}