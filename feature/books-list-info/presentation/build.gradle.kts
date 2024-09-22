plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:app-config"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui"))
                implementation(project(":feature:books-list-info:domain"))
                implementation(project(":feature:book-creator:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.books_list_info.presentation"
}