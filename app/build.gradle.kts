import java.util.Properties
import java.io.FileInputStream

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

        // Auth0 manifest placeholders (aunque aún no lo uses, no molesta)
        manifestPlaceholders["auth0Domain"] = "@string/com_auth0_domain"
        manifestPlaceholders["auth0Scheme"] = "com.ximena.foodieapp"

        // Spoonacular API key (desde local.properties)
        val localProperties = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) load(FileInputStream(file))
        }
        val spoonacularKey = localProperties.getProperty("SPOONACULAR_API_KEY") ?: ""

        buildConfigField(
            "String",
            "SPOONACULAR_API_KEY",
            "\"$spoonacularKey\""
        )
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
