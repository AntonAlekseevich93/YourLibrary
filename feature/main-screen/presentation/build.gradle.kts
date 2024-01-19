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
                implementation(project(":feature:main-screen:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.main_screen.presentation"
}