plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // ksp erzeugt den Room Code im Hintergrund beim Bauen
    alias(libs.plugins.ksp)
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
    // compose Basis für unsere Oberfläche
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // coil lädt Buchcover direkt aus der URL in Compose
    implementation(libs.coil.compose)

    // room kümmert sich um lokale Daten und den passenden Kotlin Support
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)

    // retrofit plus okhttp übernehmen API Anfragen und Logging in Logcat
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    // der Room Compiler baut die Datenbankklassen automatisch beim Build
    ksp(libs.androidx.room.compiler)

    // Testbasis für normale Unit Tests und Coroutines
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android und Compose Tests für spätere UI Prüfungen
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Debug Tools für Preview und Compose Test Umgebung
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
