plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
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
    namespace = "ru.yourlibrary.yourlibrary.common.utils.date"
}