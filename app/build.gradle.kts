plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.example.donorbox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.donorbox"
        minSdk = 24
        targetSdk = 34
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Koin for Android
    implementation(libs.koin.android)

    // koinViewModel() and Koin for Jetpack Compose (if using Jetpack Compose)
    implementation (libs.koin.androidx.compose)

    //collectAsStateWithLifecycle()
    implementation(libs.androidx.lifecycle.runtime.compose)

    //NavHost()
    implementation(libs.androidx.navigation.compose)

    //bottom navigation and rememberPullRefreshState()
    implementation(libs.androidx.material)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    //Constraint Layout in Compose
    implementation (libs.androidx.constraintlayout.compose)

    //splashScreen
    implementation (libs.androidx.core.splashscreen)

    //add icons
    implementation (libs.androidx.material.icons.extended) // Or the latest version

    //coil to display images as url
    implementation(libs.coil.compose)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)
    //Firebase Authentication
    implementation(libs.firebase.auth)
    //read data from Firebase
    implementation (libs.firebase.database.ktx)
}