plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Room necesita un "compiler" para generar código.
    id("kotlin-kapt")
}

android {
    namespace = "com.ximena.foodieapp"

    // Esto lo tenía ya así, lo dejo igual
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.ximena.foodieapp"
        minSdk = 24
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Para el proyecto no hace falta minificar ni ofuscar
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Java 11 (es el que venía por defecto en mi proyecto)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // Compose obligatorio (sin XML)
    buildFeatures {
        compose = true
    }
}

dependencies {
    // -------------------------
    // DEPENDENCIAS BASE (ya venían)
    // -------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM: gestiona versiones automáticamente (mejor que poner números a mano)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // -------------------------
    // NAVIGATION COMPOSE (obligatorio)
    // -------------------------
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // -------------------------
    // VIEWMODEL + STATE (obligatorio para MVVM)
    // -------------------------
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // -------------------------
    // COROUTINES (para cosas asíncronas: Room y Retrofit)
    // -------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // -------------------------
    // ROOM (base de datos local) - obligatorio
    // Usamos KAPT para el compiler (en vez de KSP)
    // -------------------------
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // -------------------------
    // RETROFIT (API REST) - obligatorio
    // -------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp (para logs y peticiones)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // -------------------------
    // AUTH0 (login) - obligatorio
    // -------------------------
    implementation("com.auth0.android:auth0:2.10.2")

    // -------------------------
    // COIL (cargar imágenes en Compose)
    // -------------------------
    implementation("io.coil-kt:coil-compose:2.5.0")

    // -------------------------
    // TESTS (ya venían)
    // -------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}