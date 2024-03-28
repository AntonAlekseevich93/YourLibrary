package di.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal val ktorModule = DI.Module("ktorModule") {
    bind<HttpClient>() with singleton {
        HttpClient(HttpEngineFactory().createEngine()) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                connectTimeoutMillis = 15000 //15сек
                requestTimeoutMillis = 30000
            }

//            defaultRequest {
//                url("http://185.84.162.166:8080/")
//            }
        }
    }
}