plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(deps.bundles.sketchImageLoader)
                implementation(project(":common:core"))
                implementation(project(":common:ui"))
                implementation(project(":common:models"))
                implementation(project(":common:arch"))
                implementation(project(":common:scopes"))
                implementation(project(":common:root-app:api"))
                implementation(project(":common:root-app:domain"))
                implementation(project(":feature:settings:presentation"))
                implementation(project(":feature:user:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.presentation"
}