plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jaroapps.stopwatchforcompetitions"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.jaroapps.stopwatchforcompetitions"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 121
        versionName = "1.21"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)
    testImplementation(libs.unitTests.junit)
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    implementation(libs.fragment.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.gson)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.apache.poi)
    implementation(libs.apache.poi.ooxml)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.zxing.android.embedded)
    implementation(libs.core)
}