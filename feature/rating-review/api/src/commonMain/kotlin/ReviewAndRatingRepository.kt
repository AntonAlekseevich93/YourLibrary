import kotlinx.coroutines.flow.Flow
import main_models.rating_review.ReviewAndRatingTimestampVo
import main_models.rating_review.ReviewAndRatingVo

interface ReviewAndRatingRepository {
    suspend fun getNotSynchronizedReviewAndRating(userId: Long): List<ReviewAndRatingVo>

    suspend fun getReviewAndRatingTimestamp(userId: Long): ReviewAndRatingTimestampVo
    suspend fun addOrUpdateLocalReviewAndRating(
        reviewAndRating: List<ReviewAndRatingVo>,
        userId: Long
    )

    suspend fun updateReviewAndRatingTimestamp(timestamp: ReviewAndRatingTimestampVo)

    suspend fun getCurrentUserLocalReviewAndRatingByBook(bookId: String): Flow<ReviewAndRatingVo?>

    suspend fun getAllRemoteReviewsAndRatingsByBookId(bookId: String): List<ReviewAndRatingVo>
}