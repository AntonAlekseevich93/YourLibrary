plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.SqlDelight.coroutinesExtension)
                api(project(":common:root-app:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}