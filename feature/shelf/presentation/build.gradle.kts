plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.Kamel.imageLoader)
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui")) //todo this is not good. Presentations have info about ui layer
                implementation(project(":feature:shelf:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.shelf.presentation"
}