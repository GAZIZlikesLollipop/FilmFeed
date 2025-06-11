plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.app.filmfeed"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.filmfeed"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(libs.androidx.navigation.compose.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.material.icons.extended)
    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    //Exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.ui.compose)
    //Ktor
    implementation(libs.ktor.client.core) // Используйте последнюю стабильную версию Ktor
    implementation(libs.ktor.client.android) // Движок для Android
    implementation(libs.ktor.client.content.negotiation) // Для работы с JSON
    implementation(libs.ktor.serialization.kotlinx.json) // Для сериализации/десериализации JSON с kotlinx.serialization
    implementation(libs.ktor.client.logging) // <-- Убедитесь, что эта строка есть!
    implementation(libs.ktor.client.okhttp) // <-- ДОБАВЬТЕ ЭТУ СТРОКУ для плагинов!
    // Kotlinx Serialization (если еще нет)
    implementation(libs.kotlinx.serialization.json)
}