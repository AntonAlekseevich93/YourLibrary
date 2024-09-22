import kotlinx.coroutines.flow.Flow
import main_models.BookVo
import main_models.ReadingStatus

interface BooksListInfoRepository {
    suspend fun getLocalBookByLocalId(bookLocalId: Long): Flow<BookVo?>
    suspend fun getLocalBookByIdFlow(bookId: String): Flow<BookVo?>
    suspend fun getLocalBookById(bookId: String): BookVo?
    suspend fun getBookReadingStatus(bookId: String): ReadingStatus?
}