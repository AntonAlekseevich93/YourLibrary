import kotlinx.coroutines.flow.Flow
import main_models.BookVo

interface BookInfoRepository {
    suspend fun synchronizeBooksWithAuthors(): Boolean
    suspend fun getLocalBookById(bookLocalId: Long): Flow<BookVo?>
    suspend fun updateUserBook(book: BookVo)
}