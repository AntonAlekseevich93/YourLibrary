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
                implementation(project(":common:models"))
                implementation(project(":feature:user:api"))
                implementation(project(":feature:book-info:api"))
                implementation(project(":feature:search:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.user.domain"
}