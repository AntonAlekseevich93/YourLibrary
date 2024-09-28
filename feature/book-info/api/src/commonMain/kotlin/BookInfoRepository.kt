import kotlinx.coroutines.flow.Flow
import main_models.BookVo
import main_models.books.BookTimestampVo
import main_models.rest.books.UserBookRemoteDto

interface BookInfoRepository {
    suspend fun getLocalBookByLocalId(bookLocalId: Long): Flow<BookVo?>
    suspend fun getLocalBookById(bookId: String): Flow<BookVo?>
    suspend fun updateUserBook(book: BookVo)
    suspend fun getBookTimestamp(userId: Int): BookTimestampVo
    suspend fun addOrUpdateLocalBooks(
        books: List<UserBookRemoteDto>,
        userId: Int
    )

    suspend fun updateBookTimestamp(lastTimestamp: BookTimestampVo)
    suspend fun getNotSynchronizedBooks(userId: Int): List<UserBookRemoteDto>
}