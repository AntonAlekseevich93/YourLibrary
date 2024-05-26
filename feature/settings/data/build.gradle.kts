plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.Ktor.serialization)
                api(project(":feature:settings:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:file-manager"))
                implementation(project(":common:utils:json-utils"))

            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.settings.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}