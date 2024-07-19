import kotlinx.coroutines.flow.Flow
import main_models.BookVo

interface ShelfRepository {
    suspend fun getAllBooksByReadingStatus(readingStatus: String): Flow<List<BookVo>>
    suspend fun synchronizeBooksWithAuthors(): Boolean
}