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
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.base.arch"
}