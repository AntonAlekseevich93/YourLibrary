plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(project(":feature:book-info:api"))
                implementation(project(":feature:authors:api"))
                implementation(project(":feature:search:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.book_info.domain"
}