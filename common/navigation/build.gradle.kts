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
                implementation(project(":common:core"))
                implementation(project(":common:scopes"))
                implementation(project(":common:models"))
                implementation(project(":feature:books-list-info:presentation"))
                implementation(project(":feature:book-info:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.navigation"
}