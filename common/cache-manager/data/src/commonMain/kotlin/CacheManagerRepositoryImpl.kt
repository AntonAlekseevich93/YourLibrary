import database.CacheManagerDataSource
import database.room.entities.cache.CacheBookByAuthorEntity
import database.room.entities.cache.toBookShortDtoCache
import main_models.books.BookShortDtoCache
import main_models.books.toCacheShortBook
import main_models.rest.books.BookShortRemoteDto
import platform.PlatformInfoData

class CacheManagerRepositoryImpl(
    private val cacheManagerDataSource: CacheManagerDataSource,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
    private val platformInfo: PlatformInfoData,
) : CacheManagerRepository {
    override suspend fun getCacheAllAuthorBooks(
        authorId: String,
    ): List<BookShortDtoCache> =
        cacheManagerDataSource.getCacheAllAuthorBooks(
            authorId = authorId, userId = appConfig.userId,
        ).map { it.toBookShortDtoCache() }

    override suspend fun saveAllAuthorsBooks(authorId: String, books: List<BookShortRemoteDto>) {
        val userId = appConfig.userId
        val currentTimestamp = platformInfo.getCurrentTime().timeInMillis
        val cacheBookByAuthorEntity = books.map {
            CacheBookByAuthorEntity(
                cacheBook = it.toCacheShortBook(),
                cacheAuthorId = authorId,
                cacheTimestamp = currentTimestamp,
                cacheUserId = userId
            )
        }
        cacheManagerDataSource.saveAllAuthorsBooks(
            authorId = authorId,
            userId = userId,
            books = cacheBookByAuthorEntity
        )
    }

    override suspend fun clearAllCache() {
        cacheManagerDataSource.clearAllCache()
    }

}