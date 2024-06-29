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
                implementation(project(":feature:admin:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.admin.domain"
}