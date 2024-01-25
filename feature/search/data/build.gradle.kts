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
                implementation(project(":common:utils:database-utils"))
            }
        }
    }
}


android {
    namespace = "ru.yourlibrary.yourlibrary.search.data"
}

