plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.FilePicker.filePicker)
                implementation(Dependencies.PreCompose.preComposeWithNavigation)
                implementation(Dependencies.Kamel.imageLoader)
                implementation(project(":common:core"))
                implementation(project(":common:root-app:presentation"))
                implementation(project(":common:theme"))
                implementation(project(":common:models"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:main-screen:compose"))
                implementation(project(":feature:shelf:compose"))
                implementation(project(":feature:search:compose"))
                implementation(project(":feature:book-info:compose"))
                implementation(project(":feature:book-creator:compose"))
                implementation(project(":feature:settings:compose"))
                implementation(project(":feature:authors:compose"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root-app.api"
}