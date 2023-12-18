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
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:search:api"))
                implementation(project(":feature:search:presentation"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.search.api"
}
