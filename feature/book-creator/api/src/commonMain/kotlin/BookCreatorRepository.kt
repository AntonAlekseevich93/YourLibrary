import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.BookVo

interface BookCreatorRepository {
    suspend fun parseBookUrl(url: String): BookItemResponse

    @Deprecated("replaced by room db")
    suspend fun createBook(bookItemVo: BookItemVo)
    suspend fun createBook(book: BookVo)
}