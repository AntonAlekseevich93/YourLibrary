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
                implementation(project(":common:app-config"))
                implementation(project(":common:models"))
                implementation(project(":common:arch"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui"))
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources"))
                implementation(project(":feature:rating-review:api"))
                implementation(project(":feature:rating-review:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.rating_review.presentation"
}