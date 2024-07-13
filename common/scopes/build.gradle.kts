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
                implementation(project(":common:models"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.scopes"
}