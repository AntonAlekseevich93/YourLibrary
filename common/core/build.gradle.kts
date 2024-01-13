plugins {
    id("multiplatform-setup") // используем это потому что это кор и здесь не будет ui
    id("android-setup")
    kotlin("plugin.serialization")
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                api(Dependencies.Kotlin.Serialization.serialization)
                api(Dependencies.Kotlin.Coroutines.core)

                api(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.json)
                implementation(Dependencies.Ktor.negotiation)
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.Ktor.logging)

                api(Dependencies.Kodein.core)

                implementation(Dependencies.SqlDelight.runtime)
                api(Dependencies.SqlDelight.coroutinesExtension)
            }
        }

        androidMain {
            dependencies {
                implementation(Dependencies.SqlDelight.android)
                implementation(Dependencies.Ktor.android)
            }
        }

        iosMain {
            dependencies {
                implementation(Dependencies.Ktor.ios)
                implementation(Dependencies.SqlDelight.ios)
            }
        }

        desktopMain {
            dependencies {
                implementation(Dependencies.Ktor.desktop)
                implementation(Dependencies.Ktor.okhttp)
                implementation(Dependencies.SqlDelight.desktop)
            }
        }
    }
}

android {
    namespace = "ru.yourlibrary.yourlibrary"
}
