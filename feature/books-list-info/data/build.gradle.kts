plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:books-list-info:api"))
                api(project(":feature:authors:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:app-config"))
                implementation(project(":common:http-client"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.books_list_info.data"
}