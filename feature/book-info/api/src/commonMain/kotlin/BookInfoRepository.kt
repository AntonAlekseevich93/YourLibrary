import kotlinx.coroutines.flow.Flow
import main_models.BookItemVo
import main_models.ReadingStatus
import main_models.path.PathInfoVo

interface BookInfoRepository {
    suspend fun getSelectedPathInfo(): Flow<PathInfoVo?>
    suspend fun getBookById(bookId: String): Flow<BookItemVo>
    suspend fun updateBook(bookItem: BookItemVo)
    suspend fun changeBookStatusId(readingStatus: ReadingStatus, bookId: String)

}