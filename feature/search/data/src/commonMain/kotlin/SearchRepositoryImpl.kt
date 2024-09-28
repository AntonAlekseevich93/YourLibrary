import database.LocalSearchDataSource
import database.room.entities.toVo
import ktor.RemoteSearchDataSource
import main_models.AuthorVo
import main_models.BookVo
import main_models.books.BookShortVo
import main_models.books.toRemoteDto
import main_models.rest.authors.toAuthorVo
import main_models.rest.books.toVo

class SearchRepositoryImpl(
    private val localSearchDataSource: LocalSearchDataSource,
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val remoteConfig: RemoteConfig,
    private val cacheManagerRepository: CacheManagerRepository,
) : SearchRepository {

    override suspend fun searchInAuthorsName(searchedText: String): List<AuthorVo> {
        val response = remoteSearchDataSource.getAllMatchesByAuthorName(searchedText)

        return if (response?.result == null) {
            emptyList()
        } else {
            val result = response.result!!.authors.mapNotNull { it.toAuthorVo() }
            sortAuthors(authors = result, searchedText)
        }
    }

    override suspend fun searchInBooks(uppercaseBookName: String): List<BookShortVo> {
        val response =
            remoteSearchDataSource.getAllMatchesByBookName(searchedText = uppercaseBookName)
        return if (response?.result == null) {
            emptyList()
        } else {
            val books = response.result!!.books.mapNotNull {
                it.toVo(
                    imageUrl = remoteConfig.getImageUrl(
                        imageName = it.imageName,
                        imageFolderId = it.imageFolderId,
                        bookServerId = it.id
                    ),
                )
            }
            sortBooks(books, sortedName = uppercaseBookName)
        }
    }

    override suspend fun getAllBooksByAuthor(id: String): List<BookShortVo> {
        val cachedBooks = cacheManagerRepository.getCacheAllAuthorBooks(authorId = id)
        val books = if (cachedBooks.isEmpty()) {
            val remoteBooks = remoteSearchDataSource.getAllBooksByAuthor(id)?.result?.books
            cacheManagerRepository.saveAllAuthorsBooks(
                authorId = id,
                books = remoteBooks ?: emptyList()
            )
            remoteBooks
        } else {
            cachedBooks.mapNotNull { it.toRemoteDto() }
        }

        return books?.mapNotNull {
            it.toVo(
                imageUrl = remoteConfig.getImageUrl(
                    imageName = it.imageName,
                    imageFolderId = it.imageFolderId,
                    bookServerId = it.id
                ),
            )
        } ?: emptyList()
    }

    override suspend fun searchInLocalBooks(searchText: String): List<BookVo> =
        localSearchDataSource.searchInBooks(searchText).map { book ->
            book.toVo(
                remoteImageLink = remoteConfig.getImageUrl(
                    imageName = book.imageName,
                    imageFolderId = book.imageFolderId,
                    bookServerId = book.serverId
                )
            )
        }

    /** Если искомое слово "Стивен", вначале идут все которые первым словом содержат "Стивен",
     * Затем идут все (сортированные по алфавиту) которые вторым или третьим и.д. словом содержат "Стивен"
     * В конце идут все (по алфавиту), которые содержат внутри себя "Стивен" пример - "Стивенсон"
     **/
    private fun sortAuthors(authors: List<AuthorVo>, sortedName: String): List<AuthorVo> {
        val upperSortedName = sortedName.uppercase()
        val authorsWithExactName = authors.filter {
            it.uppercaseName.split(" ").any { word -> word == upperSortedName }
        }

        val otherAuthors = authors.filter {
            !it.uppercaseName.split(" ").any { word -> word == upperSortedName }
        }

        val authorsByPosition = authorsWithExactName.groupBy {
            it.uppercaseName.split(" ").indexOf(upperSortedName)
        }

        val sortedAuthorsWithExactName = authorsByPosition.toSortedMap().flatMap {
            val position = it.key
            val authorsAtPosition = it.value

            if (position > 0) {
                authorsAtPosition.sortedBy { author ->
                    val words = author.uppercaseName.split(" ")
                    words[position - 1]
                }
            } else {
                authorsAtPosition
            }
        }

        return sortedAuthorsWithExactName + otherAuthors.sortedBy { it.name }
    }

    private fun sortBooks(books: List<BookShortVo>, sortedName: String): List<BookShortVo> {
        val upperSortedName = sortedName.uppercase()
        val booksWithExactName = books.filter {
            it.bookName.uppercase().replace(".", "").split(" ")
                .any { word -> word == upperSortedName }
        }

        val otherBooks = books.filter {
            !it.bookName.uppercase().replace(".", "").split(" ")
                .any { word -> word == upperSortedName }
        }

        val booksByPosition = booksWithExactName.groupBy {
            it.bookName.uppercase().replace(".", "").split(" ").indexOf(upperSortedName)
        }

        val sortedBooksWithExactName = booksByPosition.toSortedMap().flatMap {
            val position = it.key
            val booksAtPosition = it.value

            if (position > 0) {
                booksAtPosition.sortedBy { book ->
                    val words = book.bookName.uppercase().replace(".", "").split(" ")
                    words[position - 1]
                }
            } else {
                booksAtPosition
            }
        }

        return sortedBooksWithExactName + otherBooks.sortedBy { it.bookName }
    }
}