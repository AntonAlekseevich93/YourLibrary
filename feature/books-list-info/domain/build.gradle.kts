plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(project(":feature:books-list-info:api"))
                implementation(project(":feature:authors:api"))
                implementation(project(":feature:search:api"))
                implementation(project(":feature:rating-review:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.books_list_info.domain"
}