// gemeinsames Build Zeug für alle Module
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // ksp muss hier schon rein, sonst fehlt Room später im App Modul
    alias(libs.plugins.ksp) apply false
}
