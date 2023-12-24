plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:book-info:api"))
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.data"
}