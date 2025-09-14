
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    //new project add this
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.beyondlimits"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.beyondlimits"
        minSdk = 33
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
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.material3.android)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Jetpack Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Jetpack Compose Core komponensek
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)

    // Navigation (Compose)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.runtime:runtime:1.8.2") // Example: Replace with correct version
    // Firebase Firestore + Auth
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    // Kotlinx Coroutines + Play Services
    implementation(libs.coroutines.play.services)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Exposed (lokális SQLite DB)
    implementation(libs.sqlite.jdbc)

    // Splash screen
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Coil – képekhez
    implementation(libs.coil.compose)
    implementation("com.google.maps.android:maps-compose:6.4.1")

    implementation("com.google.accompanist:accompanist-drawablepainter:0.35.0-alpha")

}