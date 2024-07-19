plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:shelf:api"))
                api(project(":feature:book-info:api"))
                implementation(project(":common:app-config"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
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