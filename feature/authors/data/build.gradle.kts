plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:authors:api"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:app-config"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.authors.data"
}
dependencies {
    implementation(project(mapOf("path" to ":common:ui")))
}