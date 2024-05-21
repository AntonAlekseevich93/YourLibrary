import main_models.AuthorVo
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.BookVo
import main_models.books.BookShortVo

class BookCreatorInteractor(
    private val repository: BookCreatorRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun parseBookUrl(url: String): BookItemResponse = repository.parseBookUrl(url)

    suspend fun searchInAuthorsNameWithRelates(authorName: String): List<AuthorVo> {
        val response = searchRepository.searchInAuthorsName(authorName)
        val finishedSet = mutableSetOf<AuthorVo>()
        response.forEach { author ->
            if (author.isMainAuthor) {
                val relates = authorRepository.getAllRelatedAuthors(author.id)
                finishedSet.add(author.apply { relatedAuthors = relates })
            } else if (author.relatedToAuthorId != null) {
                authorRepository.getAuthorByIdWithoutRelates(author.relatedToAuthorId!!)
                    ?.let { mainAuthor ->
                        val relates =
                            authorRepository.getAllRelatedAuthors(mainAuthor.id)
                        finishedSet.add(mainAuthor.apply { relatedAuthors = relates })
                    }
            }
        }
        return finishedSet.toList()
    }

    @Deprecated("replaced by room db")
    suspend fun createBook(bookItemVo: BookItemVo) {
        repository.createBook(bookItemVo)
    }

    suspend fun createBook(book: BookVo) {
        repository.createBook(book)
    }

    suspend fun createAuthor(author: AuthorVo) {
        authorRepository.createAuthor(author)
    }

    suspend fun searchInBooks(uppercaseBookName: String): List<BookShortVo> =
        searchRepository.searchInBooks(uppercaseBookName)

    suspend fun getAllBooksByAuthor(id: String): List<BookShortVo> =
        searchRepository.getAllBooksByAuthor(id)
}