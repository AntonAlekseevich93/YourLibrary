plugins {
    id("multiplatform-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                implementation(Dependencies.SqlDelight.coroutinesExtension)
                implementation(deps.bundles.sketchImageLoader)
                api(project(":common:core"))
                implementation(project(":common:root-app:presentation"))
                api(project(":common:root-app:domain"))
                api(project(":common:root-app:api"))
                api(project(":common:root-app:data"))
                implementation(project(":common:app-config"))
                implementation(project(":common:theme"))
                implementation(project(":common:resources:drawable")) //todo возможно потом удалить
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources"))
                implementation(project(":common:http-client"))
                implementation(project(":common:ui"))
                implementation(project(":common:scopes"))
                implementation(project(":common:arch"))
                implementation(project(":common:di:compose"))
                implementation(project(":common:file-manager"))
                implementation(project(":feature:main-screen:data")) //используется из-за модуля
                implementation(project(":feature:main-screen:presentation"))
                implementation(project(":feature:shelf:data")) //используется из-за модуля
                implementation(project(":feature:search:data"))
                implementation(project(":feature:book-info:data"))
                implementation(project(":feature:book-info:domain"))
                implementation(project(":feature:book-creator:data"))
                implementation(project(":feature:book-creator:domain"))
                implementation(project(":common:utils:url-parser:data"))
                implementation(project(":common:utils:json-utils"))
                implementation(project(":feature:settings:data"))
                implementation(project(":feature:settings:presentation"))
                implementation(project(":feature:authors:data"))
                implementation(project(":feature:authors:domain"))
                implementation(project(":feature:admin:data"))
                implementation(project(":feature:admin:domain"))
                implementation(project(":feature:user:data"))
                implementation(project(":feature:user:domain"))
                implementation(project(":feature:rating-review:data"))
                implementation(deps.compose.runtime)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.root_app.api"
}