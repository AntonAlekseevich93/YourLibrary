import main_models.AuthorVo
import main_models.BookVo
import main_models.ReadingStatus

interface BookCreatorRepository {
    suspend fun createBook(book: BookVo, author: AuthorVo)
    suspend fun getBookStatusByBookId(bookId: String): ReadingStatus?
    suspend fun getLocalBookById(bookId: String): BookVo?
    suspend fun getLocalAuthorById(authorId: String): AuthorVo?
}