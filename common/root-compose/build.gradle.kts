plugins{
    id("multiplatform-compose-setup")
    id("android-setup")
}

android {
    namespace = "ru.yourlibrary.yourlibrary"
}

kotlin {
    sourceSets{
        commonMain {
            dependencies {
                api(project(":common:core"))
                implementation(project(":common:theme"))
                implementation(project(":common:resources:drawable")) //todo возможно потом удалить
                implementation(project(":common:ui"))
                implementation(project(":common:di:compose"))
                implementation(project(":feature:shelf:data")) //используется из-за модуля
                implementation(project(":feature:shelf:compose"))
                implementation(project(":feature:search:compose"))
                implementation(project(":feature:search:data"))
                implementation(project(":feature:book:compose"))
                implementation(project(":feature:book:data"))
            }
        }
    }
}