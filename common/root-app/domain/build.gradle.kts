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
                implementation(project(":common:root-app:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root-app.domain"
}