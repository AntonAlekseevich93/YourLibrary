import com.russhwolf.settings.Settings
import database.CacheManagerDataSource
import database.room.entities.cache.CacheBookByAuthorEntity
import database.room.entities.cache.CacheReviewAndRatingEntity
import database.room.entities.cache.toCacheVo
import main_models.books.CacheShortBookByAuthorVo
import main_models.rest.books.BookShortRemoteDto
import main_models.rest.rating_review.CacheReviewAndRatingVo
import main_models.rest.rating_review.ReviewAndRatingRemoteDto
import platform.PlatformInfoData

class CacheManagerRepositoryImpl(
    private val cacheManagerDataSource: CacheManagerDataSource,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
    private val platformInfo: PlatformInfoData,
) : CacheManagerRepository {

    private val localStorage = Settings()

    override suspend fun getCacheAllAuthorBooks(
        authorId: String,
    ): List<CacheShortBookByAuthorVo> =
        cacheManagerDataSource.getCacheAllAuthorBooks(
            authorId = authorId, userId = appConfig.userId,
        ).map { it.toCacheVo() }

    override suspend fun saveAllAuthorsBooks(authorId: String, books: List<BookShortRemoteDto>) {
        val userId = appConfig.userId
        val currentTimestamp = platformInfo.getCurrentTime().timeInMillis
        val cacheBookByAuthorEntity = books.takeIf { it.isNotEmpty() }?.map {
            CacheBookByAuthorEntity(
                cacheBook = it,
                cacheAuthorId = authorId,
                cacheTimestamp = currentTimestamp,
                cacheUserId = userId
            )
        } ?: listOf( //we save empty data so as not to pull an empty request from the backend
            CacheBookByAuthorEntity(
                cacheBook = null,
                cacheAuthorId = authorId,
                cacheTimestamp = currentTimestamp,
                cacheUserId = userId
            )
        )
        cacheManagerDataSource.saveAllAuthorsBooks(
            authorId = authorId,
            userId = userId,
            books = cacheBookByAuthorEntity
        )
    }

    override suspend fun getCacheReviewsAndRatingsByBook(mainBookId: String): List<CacheReviewAndRatingVo> =
        cacheManagerDataSource.getCacheAllReviewsAndRatingsByBook(
            mainBookId = mainBookId, userId = appConfig.userId,
        ).map { it.toCacheVo() }

    override suspend fun saveAllReviewsAndRatingsByBook(
        mainBookId: String,
        reviewsAndRatings: List<ReviewAndRatingRemoteDto>
    ) {
        val userId = appConfig.userId
        val currentTimestamp = platformInfo.getCurrentTime().timeInMillis
        val cacheReviewsAndRatingsEntity = reviewsAndRatings.takeIf { it.isNotEmpty() }?.map {
            CacheReviewAndRatingEntity(
                cacheReviewAndRating = it,
                cacheMainBookId = mainBookId,
                cacheTimestamp = currentTimestamp,
                cacheUserId = userId
            )
        } ?: listOf( //we save empty data so as not to pull an empty request from the backend
            CacheReviewAndRatingEntity(
                cacheReviewAndRating = null,
                cacheMainBookId = mainBookId,
                cacheTimestamp = currentTimestamp,
                cacheUserId = userId
            )
        )
        cacheManagerDataSource.saveAllReviewAndRatingByBook(
            mainBookId = mainBookId,
            userId = userId,
            reviewsAndRatings = cacheReviewsAndRatingsEntity
        )
    }

    override suspend fun clearAllCache() {
        cacheManagerDataSource.clearAllCache()
    }

    override suspend fun clearBooksByAuthorCache(authorId: String) {
        cacheManagerDataSource.clearBooksByAuthorCache(authorId, userId = appConfig.userId)
    }

    override fun saveReviewText(text: String, bookId: String) {
        localStorage.putString(key = bookId, value = text)
    }

    override fun getReviewText(bookId: String): String {
        return localStorage.getString(key = bookId, defaultValue = "")
    }

    override fun clearReviewText(bookId: String) {
        localStorage.putString(key = bookId, value = "")
    }

}