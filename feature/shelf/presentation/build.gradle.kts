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
                implementation(project(":feature:shelf:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.shelf.presentation"
}