plugins {
    id("multiplatform-setup") // используем это потому что это кор и здесь не будет ui
    id("android-setup")
    kotlin("plugin.serialization")
    alias(deps.plugins.room)
    id("com.google.devtools.ksp")
    alias(deps.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:models"))
                api(Dependencies.Kotlin.Serialization.serialization)
                api(Dependencies.Kotlin.Coroutines.core)
                implementation(deps.compose.runtime)
                api(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.json)
                implementation(Dependencies.Ktor.negotiation)
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.Ktor.logging)
                api(Dependencies.Kodein.core)

                implementation(Dependencies.SqlDelight.runtime)
                api(Dependencies.SqlDelight.coroutinesExtension)

                implementation(deps.androidx.room.runtime)
                implementation(deps.sqlite.bundled)
                implementation(deps.sqlite)
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

dependencies {
    add("kspAndroid", deps.androidx.room.compiler)
    add("kspDesktop", deps.androidx.room.compiler)
//    add("kspIosSimulatorArm64", deps.androidx.room.compiler)
//    add("kspIosX64", deps.androidx.room.compiler)
//    add("kspIosArm64", deps.androidx.room.compiler)
}

android {
    namespace = "ru.yourlibrary.yourlibrary.core"
}

room {
    schemaDirectory("$projectDir/schemas")
}