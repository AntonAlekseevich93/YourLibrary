plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:service-development:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:app-config"))
                implementation(project(":common:http-client"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.service_development.data"
}