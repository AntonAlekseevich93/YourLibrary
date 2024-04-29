plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:admin:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:http-client"))
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.admin.data"
}

dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}