
plugins {
    id("multiplatform-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(Dependencies.SqlDelight.coroutinesExtension)
                implementation(deps.compose.runtime)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.authors.api"
}