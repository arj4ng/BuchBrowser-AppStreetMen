// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // ksp braucht das Projekt oben schon, damit Room den Compiler im App Modul nutzen kann
    alias(libs.plugins.ksp) apply false
}
