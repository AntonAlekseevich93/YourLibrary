import kotlinx.coroutines.flow.Flow
import main_models.rating_review.ReviewAndRatingTimestampVo
import main_models.rating_review.ReviewAndRatingVo

interface ReviewAndRatingRepository {
    suspend fun getNotSynchronizedReviewAndRating(userId: Int): List<ReviewAndRatingVo>

    suspend fun getReviewAndRatingTimestamp(userId: Int): ReviewAndRatingTimestampVo
    suspend fun addOrUpdateLocalReviewAndRatingWhenSync(
        reviewAndRating: List<ReviewAndRatingVo>,
        userId: Int
    )

    suspend fun updateReviewAndRatingTimestamp(timestamp: ReviewAndRatingTimestampVo)

    suspend fun getCurrentUserLocalReviewAndRatingByBookFlow(mainBookId: String): Flow<ReviewAndRatingVo?>
    suspend fun getCurrentUserLocalReviewAndRatingByBook(mainBookId: String): ReviewAndRatingVo?

    suspend fun getAllRemoteReviewsAndRatingsByBookId(mainBookId: String): List<ReviewAndRatingVo>

    suspend fun addOrUpdateRatingByBookId(
        newRating: Int,
        bookId: String,
        bookAuthorId: String,
        bookGenreId: Int,
        isCreatedManuallyBook: Boolean,
        isServiceDevelopmentBook: Boolean,
        mainBookId: String,
    )

    suspend fun addReviewByBookId(
        reviewText: String,
        mainBookId: String,
    )
}