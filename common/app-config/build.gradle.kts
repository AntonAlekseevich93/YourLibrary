plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.Okio.fileManager)
                implementation(Dependencies.LocalSettings.settings)
                implementation(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.app_config"
}