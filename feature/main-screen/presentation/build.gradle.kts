plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui"))
                implementation(project(":feature:main-screen:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.main_screen.presentation"
}