import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import main_models.rest.base.BaseResponse
import kotlin.reflect.KClass

class HttpAppClient(
    private val httpClient: HttpClient,
    private val appConfig: AppConfig
) {

    @OptIn(InternalAPI::class, InternalSerializationApi::class)
    suspend fun <TResult : Any, TError : Any> post(
        url: String,
        bodyRequest: Any,
        resultClass: KClass<TResult>,
        errorClass: KClass<TError>
    ): BaseResponse<TResult, TError>? {
        return try {
            val response: HttpResponse = httpClient.post(getFullUrl(url)) {
                contentType(ContentType.Application.Json)
                header(TOKEN_KEY, appConfig.authToken)
                body = bodyRequest
            }
            val json: String = response.body<String>()
            val serializer =
                BaseResponse.serializer(resultClass.serializer(), errorClass.serializer())
            val baseResponse: BaseResponse<TResult, TError> =
                Json.decodeFromString(serializer, json)

            baseResponse
        } catch (e: Exception) {
            //todo log "Request encountered an error: ${e.message}"
            null
        } finally {
            httpClient.close()
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun <TResult : Any, TError : Any> get(
        url: String,
        resultClass: KClass<TResult>,
        errorClass: KClass<TError>
    ): BaseResponse<TResult, TError>? {
        return try {
            val response: HttpResponse = httpClient.get(getFullUrl(url)) {
                contentType(ContentType.Application.Json)
                header(TOKEN_KEY, appConfig.authToken)
            }
            val json: String = response.body<String>()
            val serializer =
                BaseResponse.serializer(resultClass.serializer(), errorClass.serializer())
            val baseResponse: BaseResponse<TResult, TError> =
                Json.decodeFromString(serializer, json)

            baseResponse
        } catch (e: Exception) {
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