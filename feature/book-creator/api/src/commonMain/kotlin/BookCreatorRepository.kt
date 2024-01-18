import main_models.BookItemResponse
import main_models.BookItemVo

interface BookCreatorRepository {
    suspend fun parseBookUrl(url: String): BookItemResponse
    suspend fun createBook(bookItemVo: BookItemVo)
}