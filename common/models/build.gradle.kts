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
                implementation(project(":common:resources:strings"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.models"
}
