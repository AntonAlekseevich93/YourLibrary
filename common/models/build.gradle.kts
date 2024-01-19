plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:resources:strings"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.models"
}
