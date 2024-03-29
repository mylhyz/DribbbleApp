plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "io.viper.android.dribbble"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.viper.android.dribbble"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        if (project.hasProperty("client_id")) {
            buildConfigField("String", "Client_ID", "\"${project.properties["client_id"] as String}\"")
        } else {
            buildConfigField("String", "Client_ID", "\"zxcvbnmasdfghjkl\"")
        }

        if (project.hasProperty("client_secret")) {
            buildConfigField("String", "Client_Secret", "\"${project.properties["client_secret"] as String}\"")
        } else {
            buildConfigField("String", "Client_Secret", "\"zxcvbnmasdfghjkl\"")
        }

        if (project.hasProperty("callback_url")) {
            buildConfigField("String", "Callback_URL", "\"${project.properties["callback_url"] as String}\"")
        } else {
            buildConfigField("String", "Callback_URL", "\"https://www.example.com/\"")
        }

        if (project.hasProperty("client_access_token")) {
            buildConfigField("String", "ACCESS_TOKEN", "\"${project.properties["client_access_token"] as String}\"")
        } else {
            buildConfigField("String", "ACCESS_TOKEN", "\"zxcvbnmasdfghjkl\"")
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
        viewBinding = true // 使用 ViewBinding
        buildConfig = true // 生成 BuildConfig
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.fresco)
    implementation("com.afollestad.material-dialogs:core:0.8.6.2")
    implementation("com.afollestad.material-dialogs:commons:0.8.6.2")
    implementation("com.squareup.okhttp3:okhttp:3.4.1")
    implementation("com.squareup.okhttp3:logging-interceptor:3.4.1")
    implementation("com.squareup.okio:okio:1.9.0")
    implementation("io.reactivex:rxjava:1.1.8")
    implementation("io.reactivex:rxandroid:1.2.1")
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.google.code.gson:gson:2.7")
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("com.squareup.retrofit2:adapter-rxjava:2.1.0")
    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("com.j256.ormlite:ormlite-core:5.0")
    implementation("com.j256.ormlite:ormlite-android:5.0")
    implementation("joda-time:joda-time:2.9.4")
    implementation("com.orhanobut:logger:1.15")

    implementation(project(":AndroidTagView"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}