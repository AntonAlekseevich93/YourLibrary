object Dependencies {

    object Kodein {
        const val core = "org.kodein.di:kodein-di-framework-compose:7.19.0"
    }

    object Kotlin {
        private const val version = "1.9.21"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"

        object Serialization {
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-serialization:1.9.10"
            const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1"
        }

        object Coroutines {
            private const val version = "1.7.3"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }
    }

    object Compose {
        private const val version = "1.5.11"
        const val gradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:$version"
    }

    object Ktor {
        private const val version = "2.3.3"
        const val core = "io.ktor:ktor-client-core:$version"
        const val json = "io.ktor:ktor-client-json:$version"
        const val negotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val ios = "io.ktor:ktor-client-darwin:$version"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val android = "io.ktor:ktor-client-android:$version"
        const val desktop = "io.ktor:ktor-client-cio:$version"
        const val okhttp = "io.ktor:ktor-client-okhttp:$version"
    }

    object Android {
        const val gradlePlugin = "com.android.tools.build:gradle:8.1.0"
    }

    object SqlDelight {
        private const val version = "2.0.0"
        const val gradlePlugin = "app.cash.sqldelight:gradle-plugin:$version"
        const val runtime = "app.cash.sqldelight:runtime:$version"
        const val android = "app.cash.sqldelight:android-driver:$version"
        const val ios = "app.cash.sqldelight:native-driver:$version"
        const val desktop = "app.cash.sqldelight:sqlite-driver:$version"
    }

    //imageLoader
    object Kamel {
        private const val version = "0.9.0"
        const val imageLoader = "media.kamel:kamel-image:$version"
    }

    //parsing html
    object Ksoup {
        private const val version = "0.1.0"
        const val ksoup = "com.fleeksoft.ksoup:ksoup:$version"

        // Optional: Include only if you need to use network request functions such as
        // Ksoup.parseGetRequest, Ksoup.parseSubmitRequest, and Ksoup.parsePostRequest
        const val ksoupNetwork = "com.fleeksoft.ksoup:ksoup-network:$version"
    }

    //navigation
    object PreCompose{
        private const val version = "1.5.10"
        const val preComposeWithNavigation = "moe.tlaster:precompose:$version"
    }
}