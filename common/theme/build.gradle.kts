plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:resources"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.theme"
}
