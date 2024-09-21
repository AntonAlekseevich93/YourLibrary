import main_models.books.BookShortDtoCache
import main_models.rest.books.BookShortRemoteDto

interface CacheManagerRepository {
    suspend fun getCacheAllAuthorBooks(authorId: String): List<BookShortDtoCache>
    suspend fun saveAllAuthorsBooks(authorId: String, books: List<BookShortRemoteDto>)
}