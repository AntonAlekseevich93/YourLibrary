plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:user:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.user.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}