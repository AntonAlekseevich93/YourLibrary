
plugins {
    id("multiplatform-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(Dependencies.SqlDelight.coroutinesExtension)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.user.api"
}