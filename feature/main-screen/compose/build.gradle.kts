plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.Kamel.imageLoader)
                implementation(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:models"))
                implementation(project(":common:ui"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:main-screen:api"))
                implementation(project(":feature:main-screen:presentation"))
                implementation(project(":feature:search:compose"))
                implementation(project(":feature:shelf:compose"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.main_screen.compose"
}
