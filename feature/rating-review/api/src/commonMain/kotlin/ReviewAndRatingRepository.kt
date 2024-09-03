import kotlinx.coroutines.flow.Flow
import main_models.rating_review.ReviewAndRatingTimestampVo
import main_models.rating_review.ReviewAndRatingVo

interface ReviewAndRatingRepository {
    suspend fun getNotSynchronizedReviewAndRating(userId: Long): List<ReviewAndRatingVo>

    suspend fun getReviewAndRatingTimestamp(userId: Long): ReviewAndRatingTimestampVo
    suspend fun addOrUpdateLocalReviewAndRatingWhenSync(
        reviewAndRating: List<ReviewAndRatingVo>,
        userId: Long
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
        bookForAllUsers: Boolean,
        mainBookId: String,
    )

    suspend fun addReviewByBookId(
        reviewText: String,
        mainBookId: String,
    )
}