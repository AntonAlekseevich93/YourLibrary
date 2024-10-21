
plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:cache-manager:api"))
                implementation(Dependencies.LocalSettings.settings)
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:app-config"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.cache_manager.data"
}