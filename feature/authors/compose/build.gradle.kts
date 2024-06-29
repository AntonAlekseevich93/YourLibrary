plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.Kamel.imageLoader)
                implementation(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:ui"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:arch"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:authors:data"))
                implementation(project(":feature:authors:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.authors.compose"
}
