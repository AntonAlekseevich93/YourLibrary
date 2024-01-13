import kotlinx.coroutines.flow.Flow
import main_models.BookItemVo

interface BookInfoRepository {
    suspend fun getBookById(bookId: String): Flow<BookItemVo>
}