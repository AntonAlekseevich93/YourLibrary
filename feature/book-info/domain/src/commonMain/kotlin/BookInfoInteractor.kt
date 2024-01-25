import kotlinx.coroutines.flow.Flow
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.ReadingStatus
import main_models.path.PathInfoVo

class BookInfoInteractor(
    private val repository: BookInfoRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun getBookById(bookId: String): Flow<BookItemVo> = repository.getBookById(bookId)

    suspend fun getSelectedPathInfo(): Flow<PathInfoVo?> = repository.getSelectedPathInfo()

    suspend fun updateBook(bookItem: BookItemVo) {
        repository.updateBook(bookItem)
    }

    suspend fun changeBookStatusId(readingStatus: ReadingStatus, bookId: String) {
        repository.changeBookStatusId(readingStatus = readingStatus, bookId = bookId)
    }

    suspend fun searchInAuthorsNameWithRelates(authorName: String): List<AuthorVo> {
        val response = searchRepository.searchInAuthorsName(authorName)
        val finishedSet = mutableSetOf<AuthorVo>()
        response.forEach { author ->
            if (author.isMainAuthor) {
                val relates = authorRepository.getAllRelatedAuthors(author.id)
                finishedSet.add(author.apply { relatedAuthors = relates })
            } else if (author.relatedToAuthorId != null) {
                authorRepository.getAuthorById(author.relatedToAuthorId!!)
                    ?.let { mainAuthor ->
                        val relates =
                            authorRepository.getAllRelatedAuthors(mainAuthor.id)
                        finishedSet.add(mainAuthor.apply { relatedAuthors = relates })
                    }
            }
        }
        return finishedSet.toList()
    }

    suspend fun getAuthorWithRelatesWithoutBooks(id: String): AuthorVo? =
        authorRepository.getAuthorWithRelatesWithoutBooks(id)

    suspend fun createAuthor(author: AuthorVo) {
        authorRepository.createAuthor(author)
    }

}