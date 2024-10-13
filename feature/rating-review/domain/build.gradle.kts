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
                implementation(project(":feature:rating-review:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.rating_review.domain"
}