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
                implementation(project(":common:models"))
                implementation(project(":common:root-app:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.domain"
}