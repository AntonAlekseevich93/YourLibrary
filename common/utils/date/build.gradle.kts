plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {

            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.utils.date"
}