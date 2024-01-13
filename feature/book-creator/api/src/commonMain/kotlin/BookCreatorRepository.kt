import kotlinx.coroutines.flow.Flow
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.ShelfVo

interface BookCreatorRepository {
    suspend fun parseBookUrl(url: String): BookItemResponse
    suspend fun createBook(bookItemVo: BookItemVo)
    suspend fun getShelvesWithoutBooks(): Flow<List<ShelfVo>>
}