// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Hilt: sistema de inyección de dependencias
    id("com.google.dagger.hilt.android") version "2.48" apply false
    // KSP: genera código automático para Room y Hilt
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}