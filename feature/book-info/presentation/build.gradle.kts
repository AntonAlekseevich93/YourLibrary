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
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui"))
                implementation(project(":feature:book-info:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.presentation"
}