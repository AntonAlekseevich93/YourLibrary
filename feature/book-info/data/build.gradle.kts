plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:book-info:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.data"
}