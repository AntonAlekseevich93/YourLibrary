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
                implementation(project(":common:core"))
            }
        }
    }
}


android {
    namespace = "ru.yourlibrary.yourlibrary.search.data"
}

