plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:arch"))
                implementation(project(":common:scopes"))
                implementation(project(":common:models"))
                implementation(project(":common:ui"))
                implementation(project(":feature:book-creator:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_creator.presentation"
}