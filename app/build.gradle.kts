plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.composetemplate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.composetemplate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx.v1130)
    implementation(libs.androidx.lifecycle.runtime.ktx.v283)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom.v20240600))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.firebase.config.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20240600))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation(libs.kotlinx.serialization.json)

    /* Firebase */
    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.firebase.analytics))
    implementation(libs.firebase.auth)

    /* Gson */
    implementation(libs.gson)

    /* Compose Navigation */
    implementation(libs.navigation.compose)

    /* Ktor */
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.cio)

    /* Coil */
    implementation(libs.coil.compose)

    /* Datastore */
    implementation(libs.androidx.datastore.preferences)

    /* Compose Foundation */
    implementation(libs.androidx.foundation)

    /* Paging 3 */
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    /* Room */
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    /* Koin */
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    /* DataStore */
    implementation(libs.androidx.datastore.preferences.core)
}