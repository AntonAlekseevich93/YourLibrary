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
                implementation(project(":feature:book-info:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.presentation"
}