plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(deps.bundles.sketchImageLoader)
                implementation(deps.hazeBlur)
                implementation(project(":common:core"))
                implementation(project(":common:navigation"))
                implementation(project(":common:theme"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:arch"))
                implementation(project(":common:ui"))
                implementation(project(":common:utils:date"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources"))
                implementation(project(":feature:book-creator:api"))
                implementation(project(":feature:book-creator:presentation"))
                implementation(project(":feature:books-list-info:compose"))
                implementation(project(":feature:books-list-info:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_creator.api"
}
