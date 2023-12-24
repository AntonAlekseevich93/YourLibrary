plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":feature:shelf:presentation"))
                implementation(project(":feature:search:presentation"))
                implementation(project(":feature:search:data")) //todo remove?
                implementation(project(":feature:book-info:presentation"))
                implementation(project(":feature:book-info:data")) //todo remove?
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.di.compose"
}