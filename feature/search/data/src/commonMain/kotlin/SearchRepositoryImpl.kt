import database.LocalSearchDataSource
import ktor.RemoteSearchDataSource
import main_models.AuthorVo
import main_models.books.BookShortVo
import main_models.rest.authors.toAuthorVo
import main_models.rest.books.toVo

class SearchRepositoryImpl(
    private val localSearchDataSource: LocalSearchDataSource,
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val remoteConfig: RemoteConfig
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
            val urlPrefix = remoteConfig.s3_FULL_PREFIX + remoteConfig.S3_BOOK_IMAGES_PATH
            response.result!!.books.mapNotNull { it.toVo(imagePrefixUrl = urlPrefix) }
        }
    }

    override suspend fun getAllBooksByAuthor(id: String): List<BookShortVo> {
        val response = remoteSearchDataSource.getAllBooksByAuthor(id)
        return if (response?.result == null) {
            emptyList()
        } else {
            val urlPrefix = remoteConfig.s3_FULL_PREFIX + remoteConfig.S3_BOOK_IMAGES_PATH
            response.result!!.books.mapNotNull { it.toVo(imagePrefixUrl = urlPrefix) }
        }
    }

}