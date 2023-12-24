plugins {
    id("multiplatform-setup")
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
    namespace = "ru.yourlibrary.yourlibrary.book_info.api"
}