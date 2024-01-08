import main_models.BookItemResponse

interface UrlParserInteractor {
    suspend fun parseBookUrl(url: String): BookItemResponse
}