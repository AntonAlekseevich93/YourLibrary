import kotlinx.coroutines.flow.Flow
import main_models.BookVo

interface BooksListInfoRepository {
    suspend fun getLocalBookByLocalId(bookLocalId: Long): Flow<BookVo?>
    suspend fun getLocalBookById(bookId: String): Flow<BookVo?>


}