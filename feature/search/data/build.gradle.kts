plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:search:api"))
                api(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:core"))
                implementation(project(":common:http-client"))
                implementation(project(":common:app-config"))
                implementation(project(":common:cache-manager:api"))
            }
        }
    }
}


android {
    namespace = "ru.yourlibrary.yourlibrary.search.data"
}

