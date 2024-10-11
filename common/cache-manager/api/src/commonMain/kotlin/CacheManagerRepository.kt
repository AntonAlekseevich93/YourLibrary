import main_models.books.CacheShortBookByAuthorVo
import main_models.rest.books.BookShortRemoteDto
import main_models.rest.rating_review.CacheReviewAndRatingVo
import main_models.rest.rating_review.ReviewAndRatingRemoteDto

interface CacheManagerRepository {
    suspend fun getCacheAllAuthorBooks(authorId: String): List<CacheShortBookByAuthorVo>
    suspend fun saveAllAuthorsBooks(authorId: String, books: List<BookShortRemoteDto>)
    suspend fun getCacheReviewsAndRatingsByBook(mainBookId: String): List<CacheReviewAndRatingVo>
    suspend fun saveAllReviewsAndRatingsByBook(
        mainBookId: String,
        reviewsAndRatings: List<ReviewAndRatingRemoteDto>
    )

    suspend fun clearAllCache()
    suspend fun clearBooksByAuthorCache(authorId: String)
}