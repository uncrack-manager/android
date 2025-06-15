buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath("com.android.tools.build:gradle:8.9.0")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}
plugins {
    id("com.android.application") version "8.9.0" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("com.google.devtools.ksp") version "2.1.21-2.0.2" apply false
}
allprojects {
    repositories {
        maven(url = "https://jitpack.io")
    }
}
