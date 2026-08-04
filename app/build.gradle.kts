plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // ksp baut den Room Code beim Build mit
    alias(libs.plugins.ksp)
    //Navigation
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.example.buchbrowser"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.buchbrowser"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // compose Grundzeug
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // coil lädt Cover aus der URL
    implementation(libs.coil.compose)

    // room für lokale Daten
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)

    // API Anfragen plus Logging
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    // Room Klassen kommen über den Compiler rein
    ksp(libs.androidx.room.compiler)

    // normales Test Zeug
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android und Compose Tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Debug Helfer für Preview und Tests
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("androidx.compose.material:material-icons-extended")
    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)
}
