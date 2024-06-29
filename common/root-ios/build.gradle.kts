//здесь будем настраивать cocoapods плагин
plugins {
    id("multiplatform-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

android {
    namespace = "ru.yourlibrary.yourlibrary"
}