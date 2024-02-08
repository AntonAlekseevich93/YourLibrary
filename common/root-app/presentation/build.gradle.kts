plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.Kamel.imageLoader)
                implementation(project(":common:core"))
                implementation(project(":common:ui"))
                implementation(project(":common:models"))
                implementation(project(":common:arch"))
                implementation(project(":common:scopes"))
                implementation(project(":common:root-app:api"))
                implementation(project(":common:root-app:domain"))
                implementation(project(":feature:settings:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root-app.presentation"
}