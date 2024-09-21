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
                implementation(project(":common:theme"))
                implementation(project(":common:ui"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:arch"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources"))
                implementation(project(":feature:admin:data"))
                implementation(project(":feature:admin:presentation"))
                implementation(project(":feature:books-list-info:compose"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.admin.compose"
}
