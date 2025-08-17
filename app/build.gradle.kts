import java.io.FileInputStream
import java.util.Properties

plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
    id ("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
}

val localProperties = Properties()
if (rootProject.file("local.properties").exists()) {
    localProperties.load(FileInputStream(rootProject.file("local.properties")))
}
val versionProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "version.properties")))
}

val versioncode = versionProperties["VERSION_CODE"].toString().toInt()
val versionname = versionProperties["VERSION_NAME"].toString()

android {
    namespace = "com.aritradas.uncrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aritradas.uncrack"
        minSdk = 24
        targetSdk = 34
        versionCode = versioncode
        versionName = versionname
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        baseline = file("lint-baseline.xml")
    }

    signingConfigs {
        create("release") {
            val keystorePath = localProperties.getProperty("uncrack.store.file")
                ?: System.getProperty("android.injected.signing.store.file")
                ?: "../uncrack_release.jks"

            storeFile = file(keystorePath)
            storePassword = localProperties.getProperty("uncrack.store.password")
                ?: System.getProperty("android.injected.signing.store.password")
                        ?: ""
            keyAlias = localProperties.getProperty("uncrack.key.alias")
                ?: System.getProperty("android.injected.signing.key.alias")
                        ?: ""
            keyPassword = localProperties.getProperty("uncrack.key.password")
                ?: System.getProperty("android.injected.signing.key.password")
                        ?: ""
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_name", "UnCrack")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "UnCrack Debug")
            versionNameSuffix = "-debug"
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {

    implementation("com.google.android.material:material:1.12.0")
    val roomVersion = "2.6.1"
    val viewModelVersion = "2.5.1"

    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3:1.4.0-alpha14")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0-alpha03")
    implementation("androidx.navigation:navigation-compose:2.8.6")
    implementation("androidx.compose.animation:animation:1.9.0")
    implementation("androidx.compose.animation:animation-graphics-android:1.9.0")


    // Compose Test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Default
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    // Test
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("com.google.truth:truth:1.2.0")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")

    // Room
    implementation ("androidx.room:room-runtime:$roomVersion")
    ksp ("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")

    //ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$viewModelVersion")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$viewModelVersion")

    // Kotlin components
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.22")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-work:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    // Lottie Animation
    implementation("com.airbnb.android:lottie-compose:6.6.2")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation ("com.google.firebase:firebase-crashlytics")
    implementation ("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Compose-lint
    lintChecks("com.slack.lint.compose:compose-lint-checks:1.3.1")

    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    //kotlinx.collections.immutable
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")

    // Circular Progress bar
    implementation ("com.mikhaellopez:circularprogressbar:3.1.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // In-App Update
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // BCrypt
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation("androidx.biometric:biometric:1.2.0-alpha05")
}
