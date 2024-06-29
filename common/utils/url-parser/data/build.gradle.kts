plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                api(project(":common:utils:url-parser:api"))
                implementation(project(":common:models"))
                implementation(Dependencies.Ksoup.ksoupNetwork)
                implementation(Dependencies.Ksoup.ksoup)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.url_parser.data"
}