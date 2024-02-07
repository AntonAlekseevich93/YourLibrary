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
                implementation(project(":feature:main-screen:data"))
                implementation(project(":feature:search:presentation"))
                implementation(project(":feature:search:data"))
                implementation(project(":feature:book-info:presentation"))
                implementation(project(":feature:book-info:domain"))
                implementation(project(":feature:book-creator:presentation"))
                implementation(project(":feature:book-creator:domain"))
                implementation(project(":feature:shelf:presentation"))
                implementation(project(":feature:shelf:data"))
                implementation(project(":feature:settings:presentation"))
                implementation(project(":feature:settings:data"))
                implementation(project(":feature:authors:presentation"))
                implementation(project(":feature:authors:data"))
                implementation(project(":feature:authors:domain"))
                implementation(project(":common:core"))
                implementation(project(":common:scopes"))
                implementation(project(":common:arch"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.di.compose"
}