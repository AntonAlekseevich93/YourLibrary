plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.utils.database-utils"
}