import main_models.BookItemResponse

interface BookCreatorRepository {
    suspend fun parseBookUrl(url: String): BookItemResponse

    suspend fun createBook()
}