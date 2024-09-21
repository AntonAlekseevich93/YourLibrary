plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(project(":common:cache-manager:api"))
                implementation(project(":feature:settings:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.settings.domain"
}