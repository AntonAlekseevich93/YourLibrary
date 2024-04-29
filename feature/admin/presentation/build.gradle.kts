plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:models"))
                implementation(project(":common:arch"))
                implementation(project(":common:scopes"))
                implementation(project(":common:ui"))
                implementation(project(":common:resources:strings"))
                implementation(project(":feature:admin:api"))
                implementation(project(":feature:admin:domain"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.admin.presentation"
}