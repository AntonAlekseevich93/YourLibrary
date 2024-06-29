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
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.constants"
}