import kotlinx.coroutines.flow.Flow
import main_models.BookVo
import main_models.ReadingStatus

class BookInfoInteractor(
    private val repository: BookInfoRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
) {

    suspend fun getLocalBookById(localBookId: Long): Flow<BookVo?> =
        repository.getLocalBookById(localBookId)

    suspend fun changeUserBookReadingStatus(book: BookVo, newStatus: ReadingStatus) {
        repository.updateUserBook(
            book = book.copy(readingStatus = newStatus)
        )
    }

    suspend fun getAllBooksByAuthor(authorId: String) =
        searchRepository.getAllBooksByAuthor(authorId)

}