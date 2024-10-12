plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    kotlin("plugin.serialization")
    alias(deps.plugins.compose.compiler)
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
                implementation(project(":feature:user:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.app_config"
}