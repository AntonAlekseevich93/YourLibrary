plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:ui"))
                implementation(project(":common:models"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:settings:data"))
                implementation(project(":feature:settings:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.settings.api"
}
