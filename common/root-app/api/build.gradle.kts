plugins {
    id("multiplatform-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.SqlDelight.coroutinesExtension)
                implementation(project(":common:models"))
                implementation(project(":common:core"))
                implementation(deps.compose.runtime)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.api"
}