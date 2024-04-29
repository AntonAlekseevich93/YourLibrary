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
                api(project(":feature:user:api"))
                implementation(project(":common:constants"))
                implementation(project(":common:core"))
                implementation(project(":common:http-client"))
                implementation(project(":common:models"))
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.user.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}