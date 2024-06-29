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
                implementation(Dependencies.Okio.fileManager)
                implementation(project(":common:models"))
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.file_manager"
}