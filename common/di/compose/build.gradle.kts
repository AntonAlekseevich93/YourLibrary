plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
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
                implementation(project(":feature:books-list-info:presentation"))
                implementation(project(":feature:books-list-info:domain"))
                implementation(project(":feature:book-creator:presentation"))
                implementation(project(":feature:book-creator:domain"))
                implementation(project(":feature:shelf:presentation"))
                implementation(project(":feature:shelf:data"))
                implementation(project(":feature:settings:presentation"))
                implementation(project(":feature:settings:data"))
                implementation(project(":feature:settings:domain"))
                implementation(project(":feature:authors:presentation"))
                implementation(project(":feature:authors:data"))
                implementation(project(":feature:authors:domain"))
                implementation(project(":feature:admin:presentation"))
                implementation(project(":feature:admin:data"))
                implementation(project(":feature:admin:domain"))
                implementation(project(":feature:user:presentation"))
                implementation(project(":feature:user:data"))
                implementation(project(":feature:user:domain"))
                implementation(project(":feature:rating-review:presentation"))
                implementation(project(":feature:rating-review:domain"))
                implementation(project(":common:core"))
                implementation(project(":common:app-config"))
                implementation(project(":common:scopes"))
                implementation(project(":common:arch"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.di.compose"
}