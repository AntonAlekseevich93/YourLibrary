plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependencies.SqlDelight.coroutinesExtension)
                api(project(":common:root-app:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}