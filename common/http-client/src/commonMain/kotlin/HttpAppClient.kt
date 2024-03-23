import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI

class HttpAppClient(
    private val httpClient: HttpClient,
    private val appConfig: AppConfig
) {

    @OptIn(InternalAPI::class)
    suspend fun post(
        url: String,
        bodyRequest: Any?
    ): HttpResponse? {
        return try {
            httpClient.post(getFullUrl(url)) {
                contentType(ContentType.Application.Json)
                header(TOKEN_KEY, appConfig.authToken)
                bodyRequest?.let {
                    body = it
                }

            }
        } catch (e: Exception) {
            //todo log "Request encountered an error: ${e.message}"
            null
        } finally {
            httpClient.close()
        }
    }

    private fun getFullUrl(url: String): String = "${appConfig.baseUrl}$url"

    companion object {
        private const val TOKEN_KEY = "user_token"
    }
}