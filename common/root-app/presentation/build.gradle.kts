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
                implementation(project(":common:app-config"))
                implementation(project(":common:core"))
                implementation(project(":common:ui"))
                implementation(project(":common:models"))
                implementation(project(":common:arch"))
                implementation(project(":common:scopes"))
                implementation(project(":common:root-app:api"))
                implementation(project(":common:root-app:domain"))
                implementation(project(":feature:settings:presentation"))
                implementation(project(":feature:user:domain"))
                implementation(project(":feature:book-info:presentation"))
                implementation(project(":feature:books-list-info:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.presentation"
}