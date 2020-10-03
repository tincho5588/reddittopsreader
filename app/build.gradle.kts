plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "com.tincho5588.reddittopsreader"
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.dataBinding = true

    buildTypes.getByName("release") {
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()

    testOptions.unitTests.isIncludeAndroidResources = true
}

kapt {
    correctErrorTypes = true
}

dependencies {
    val kotlin_version = "1.4.0"
    val retrofit_version = "2.9.0"
    val dagger_version = "2.28.3"
    val hilt_version = "2.29.1-alpha"
    val hilt_viewmodel_version = "1.0.0-alpha02"
    val room_version = "2.2.5"
    val glide_version = "4.11.0"
    val robolectric_version = "4.4"
    val mockito_version = "3.5.11"
    val coroutines_version = "1.3.9"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")

    // androidx components
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha05")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

    // Dagger
    implementation("com.google.dagger:dagger:$dagger_version")
    implementation("com.google.dagger:dagger-android:$dagger_version")
    kapt("com.google.dagger:dagger-android-processor:$dagger_version")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$hilt_viewmodel_version")
    kapt("androidx.hilt:hilt-compiler:$hilt_viewmodel_version")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Glide
    implementation("com.github.bumptech.glide:glide:$glide_version")
    kapt("com.github.bumptech.glide:compiler:$glide_version")

    // RecyclerView ItemAnimators
    implementation("com.mikepenz:itemanimators:1.1.0")

    implementation("com.squareup.leakcanary:leakcanary-android:2.5")

    // junit
    testImplementation("junit:junit:4.13")

    // Robolectric
    testImplementation("org.robolectric:robolectric:$robolectric_version")

    // Mockito
    testImplementation("org.mockito:mockito-core:$mockito_version")
    testImplementation("org.mockito:mockito-inline:$mockito_version")

    // Hilt unit tests
    testImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    kaptTest("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Room for Unit Tests
    testImplementation("androidx.room:room-testing:$room_version")

    // Couroutines tests library
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")

    // Android Architecture core testing library
    testImplementation("androidx.arch.core:core-testing:2.1.0")
}