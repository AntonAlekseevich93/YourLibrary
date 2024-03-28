import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import main_models.rest.base.BaseResponse
import kotlin.reflect.KClass

class HttpAppClient(
    private val httpClient: HttpClient,
    private val appConfig: AppConfig
) {

    @OptIn(InternalSerializationApi::class)
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
                setBody(bodyRequest)
            }
            val jsonAsString: String = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val serializer =
                BaseResponse.serializer(resultClass.serializer(), errorClass.serializer())
            val baseResponse: BaseResponse<TResult, TError> =
                json.decodeFromString(serializer, jsonAsString)
            baseResponse
        } catch (e: Exception) {
            println("HttpAppClient exception: ${e.message}")
            //todo log "Request encountered an error: ${e.message}"
            null
        } finally {
//            httpClient.close()
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
            val jsonAsString: String = response.body<String>()
            val json = Json { ignoreUnknownKeys = true }
            val serializer =
                BaseResponse.serializer(resultClass.serializer(), errorClass.serializer())
            val baseResponse: BaseResponse<TResult, TError> =
                json.decodeFromString(serializer, jsonAsString)

            baseResponse
        } catch (e: Exception) {
            println("GET EXSEPTIONS = ${e.message}")
            null
        } finally {
//            httpClient.close()
        }
    }

    private fun getFullUrl(url: String): String = "${appConfig.baseUrl}$url"

    companion object {
        private const val TOKEN_KEY = "user_token"
    }
}