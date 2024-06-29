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
                implementation(deps.compose.runtime)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.url_parser.api"
}