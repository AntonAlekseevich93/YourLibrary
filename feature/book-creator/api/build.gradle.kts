plugins {
    id("multiplatform-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_creator.api"
}