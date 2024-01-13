plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:shelf:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.shelf.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}