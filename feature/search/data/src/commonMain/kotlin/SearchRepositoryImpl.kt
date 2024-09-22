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
            response.result!!.authors.mapNotNull { it.toAuthorVo() }
        }
    }

    override suspend fun searchInBooks(uppercaseBookName: String): List<BookShortVo> {
        val response =
            remoteSearchDataSource.getAllMatchesByBookName(searchedText = uppercaseBookName)
        return if (response?.result == null) {
            emptyList()
        } else {
            response.result!!.books.mapNotNull {
                it.toVo(
                    imageUrl = remoteConfig.getImageUrl(
                        imageName = it.imageName,
                        imageFolderId = it.imageFolderId,
                        bookServerId = it.id
                    ),
                )
            }
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

}