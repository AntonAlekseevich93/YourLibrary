import main_models.BookVo
import main_models.ReadingStatus

interface BookCreatorRepository {
    suspend fun createBook(book: BookVo)
    suspend fun getBookStatusByBookId(bookId: String): ReadingStatus?
}