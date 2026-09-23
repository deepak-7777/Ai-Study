plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.aistudyos.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aistudyos.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Base URL configurable per build variant
        buildConfigField("String", "BASE_URL", "\"https://api.aistudyos.com/\"")
        buildConfigField("String", "APP_VERSION", "\"1.0.0\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"http://192.168.29.29:8080/\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL", "\"https://api.aistudyos.com/\"")
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
        buildConfig = true
    }
}

dependencies {
    // Core Android
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.8.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Lifecycle + ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")

//    // Hilt DI
//    implementation("com.google.dagger:hilt-android:2.51")
//    ksp("com.google.dagger:hilt-compiler:2.51")
//    implementation("androidx.hilt:hilt-work:1.2.0")
//    ksp("androidx.hilt:hilt-compiler:1.2.0")

// Hilt
implementation("com.google.dagger:hilt-android:2.51")
    implementation("com.google.android.gms:play-services-basement:18.10.0")
    kapt("com.google.dagger:hilt-compiler:2.51")

implementation("androidx.hilt:hilt-work:1.2.0")
kapt("androidx.hilt:hilt-compiler:1.2.0")


    // Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Coil (image loading)
    implementation("io.coil-kt:coil:2.6.0")

    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ViewPager2 (Onboarding)
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Shimmer loading effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // DonutChart / MPAndroidChart for analytics
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Lottie animations
    implementation("com.airbnb.android:lottie:6.4.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.tbuonomo:dotsindicator:4.3")

    /// for Google
    implementation("com.google.android.gms:play-services-auth:20.7.0")


    ///  for cloundiary
    implementation("com.cloudinary:cloudinary-android:2.5.0") {
        exclude(group = "com.facebook.fresco")
    }

    implementation("com.github.yalantis:ucrop:2.2.8")

    // Image libraries
    implementation("com.github.bumptech.glide:glide:4.15.1")
//    implementation("com.cloudinary:cloudinary-android:3.1.1")

    // UI
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

    implementation("com.github.chrisbanes:PhotoView:2.3.0")
}


