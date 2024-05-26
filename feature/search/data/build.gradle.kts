plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":feature:search:api"))
                api(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:core"))
                implementation(project(":common:http-client"))
                implementation(project(":common:app-config"))
            }
        }
    }
}


android {
    namespace = "ru.yourlibrary.yourlibrary.search.data"
}

