plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(deps.bundles.sketchImageLoader)
                implementation(deps.hazeBlur)
                implementation(deps.compottie)
                implementation(deps.compottie.resources)
                api(project(":common:core"))
                api(project(":common:navigation")) //todo это не правильно что мы видим модуль
                implementation(project(":common:utils:date"))
                implementation(project(":common:models"))
                implementation(project(":common:theme"))
                implementation(project(":common:scopes"))
                implementation(project(":common:resources:strings"))
                implementation(project(":common:resources:drawable"))
                implementation(project(":common:resources"))
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.ui"
}