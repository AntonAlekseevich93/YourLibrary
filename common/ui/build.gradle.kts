plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources:drawable"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.ui"
}