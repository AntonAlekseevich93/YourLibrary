plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(project(":feature:authors:api"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.authors.domain"
}