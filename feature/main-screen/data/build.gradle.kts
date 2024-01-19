plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:main-screen:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.main_screen.data"
}