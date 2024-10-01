plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.ksp.project)
}

android {
    namespace = "com.example.listfetcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.listfetcher"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //Jetpack compose dependencies
    val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.compiler)
    implementation(libs.compose.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.runtime)
    implementation(libs.compose.debug)
    androidTestImplementation(libs.compose.test)
    debugImplementation(libs.compose.mani)
    implementation(libs.compose.activity)
    implementation(libs.compose.vm)

    //Base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.logcat)
    implementation(libs.androidx.work)


    //Moshi
    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
    implementation(libs.moshiAdapters)
    implementation(libs.moshiConverter)
    implementation(libs.moshiAdapters)
    implementation(libs.moshiCodegen)

    //Okhttp and retrofit
    implementation(libs.okkhttp)
    implementation(libs.okkhttpLoggingInterceptor)

    //Coroutines
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)

    //Hilt
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.hilt.worker)
    implementation(libs.hilt.android)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.junit)

}
