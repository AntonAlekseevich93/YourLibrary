import main_models.AuthorVo
import main_models.BookVo
import main_models.ReadingStatus
import main_models.books.BookShortVo

class BookCreatorInteractor(
    private val repository: BookCreatorRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun searchInAuthorsNameWithRelates(authorName: String): List<AuthorVo> {
        val response = searchRepository.searchInAuthorsName(authorName)

        return response
    }

    suspend fun createBook(book: BookVo, author: AuthorVo) {
        repository.createBook(book, author = author)
    }

    suspend fun searchInBooks(uppercaseBookName: String): List<BookShortVo> =
        searchRepository.searchInBooks(uppercaseBookName)

    suspend fun getAllBooksByAuthor(id: String): List<BookShortVo> =
        searchRepository.getAllBooksByAuthor(id)

    suspend fun getBookStatusByBookId(bookId: String): ReadingStatus? =
        repository.getBookStatusByBookId(bookId)

}