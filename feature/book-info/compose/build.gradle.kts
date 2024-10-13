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
                implementation(project(":common:ui"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources"))
                implementation(project(":feature:book-info:api"))
                implementation(project(":feature:book-info:presentation"))
                implementation(project(":feature:search:compose"))
                implementation(project(":feature:rating-review:compose"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.compose"
}
