plugins {
    id("com.android.application")
    kotlin("android")
    alias(deps.plugins.compose.compiler)
}

android {
    namespace = "ru.yourlibrary.yourlibrary.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "ru.yourlibrary.yourlibrary.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Dependencies.PreCompose.preComposeWithNavigation)
    implementation(project(":common:root-app:compose"))
    implementation(project(":common:root-app:di"))
    implementation(project(":common:theme"))
    implementation(project(":common:scopes"))
    implementation(project(":common:models"))
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.foundation:foundation:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.2")
}