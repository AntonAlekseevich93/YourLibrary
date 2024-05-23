plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:book-creator:api"))
                implementation(project(":common:utils:url-parser:api"))
                implementation(project(":common:utils:database-utils"))
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:constants"))
                implementation(project(":common:app-config"))
                implementation(project(":common:http-client"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_creator.data"
}