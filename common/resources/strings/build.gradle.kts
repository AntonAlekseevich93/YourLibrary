plugins {
    id("multiplatform-compose-setup")
    id("android-setup")
    alias(deps.plugins.compose.compiler)
}

android {
    namespace = "ru.yourlibrary.yourlibrary.common.resources.string"
}