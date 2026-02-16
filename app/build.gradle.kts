import java.util.Properties
import java.io.FileInputStream
import org.gradle.api.GradleException

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // KSP para Room
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "com.ximena.foodieapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ximena.foodieapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Leer claves desde local.properties (Spoonacular + Auth0)
        val localProperties = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) load(FileInputStream(file))
        }

        // ── Spoonacular ────────────────────────────────────────────────────
        val spoonacularKey = (localProperties.getProperty("SPOONACULAR_API_KEY") ?: "").trim()
        buildConfigField("String", "SPOONACULAR_API_KEY", "\"$spoonacularKey\"")

        // ── Auth0 ──────────────────────────────────────────────────────────
        val auth0Domain = (localProperties.getProperty("AUTH0_DOMAIN") ?: "").trim()
        val auth0ClientId = (localProperties.getProperty("AUTH0_CLIENT_ID") ?: "").trim()

        // Si faltan, mejor petar aquí con mensaje claro
        if (auth0Domain.isBlank() || auth0ClientId.isBlank()) {
            throw GradleException("Faltan AUTH0_DOMAIN o AUTH0_CLIENT_ID en local.properties")
        }

        buildConfigField("String", "AUTH0_DOMAIN", "\"$auth0Domain\"")
        buildConfigField("String", "AUTH0_CLIENT_ID", "\"$auth0ClientId\"")

        // Placeholders para el intent-filter del Manifest (callback de Auth0)
        manifestPlaceholders["auth0Domain"] = auth0Domain
        manifestPlaceholders["auth0Scheme"] = "com.ximena.foodieapp"
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

    // Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // ── COMPOSE ───────────────────────────────────────────────────────────
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // ── NAVEGACIÓN ────────────────────────────────────────────────────────
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ── VIEWMODEL ─────────────────────────────────────────────────────────
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // ── ROOM ─────────────────────────────────────────────────────────────
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // ── RETROFIT ──────────────────────────────────────────────────────────
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ── AUTH0 ─────────────────────────────────────────────────────────────
    implementation("com.auth0.android:auth0:2.11.0")

    // ── TOKENS CIFRADOS ───────────────────────────────────────────────────
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // ── COROUTINES ────────────────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ── COIL ──────────────────────────────────────────────────────────────
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ── TEST y DEBUG ──────────────────────────────────────────────────────
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
