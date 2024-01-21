plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                implementation(project(":feature:shelf:presentation"))
                implementation(project(":feature:main-screen:presentation"))
                implementation(project(":feature:main-screen:data")) //todo remove?
                implementation(project(":feature:search:presentation"))
                implementation(project(":feature:search:data")) //todo remove?
                implementation(project(":feature:book-info:presentation"))
                implementation(project(":feature:book-info:data")) //todo remove?
                implementation(project(":feature:book-creator:presentation"))
                implementation(project(":feature:book-creator:data")) //todo remove?
                implementation(project(":feature:shelf:presentation"))
                implementation(project(":feature:shelf:data")) //todo remove?
                implementation(project(":feature:settings:presentation"))
                implementation(project(":feature:settings:data")) //todo remove?
                implementation(project(":feature:authors:presentation"))
                implementation(project(":feature:authors:data")) //todo remove?
                implementation(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.di.compose"
}