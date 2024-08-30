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
                implementation(project(":feature:book-info:api"))
                implementation(project(":feature:book-creator:api"))
                implementation(project(":feature:authors:api"))
                implementation(project(":feature:search:api"))
                implementation(project(":feature:rating-review:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.domain"
}