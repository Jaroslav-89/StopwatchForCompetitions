// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.safeArgs)
    }
}
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.1" apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
}