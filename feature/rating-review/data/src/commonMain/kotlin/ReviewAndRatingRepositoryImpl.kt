import database.LocalReviewAndRatingDataSource
import database.room.entities.toEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import ktor.RemoteReviewAndRatingDataSource
import main_models.rating_review.ReviewAndRatingTimestampVo
import main_models.rating_review.ReviewAndRatingVo

class ReviewAndRatingRepositoryImpl(
    private val localReviewAndRatingDataSource: LocalReviewAndRatingDataSource,
    private val remoteReviewAndRatingDataSource: RemoteReviewAndRatingDataSource,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : ReviewAndRatingRepository {

    override suspend fun getNotSynchronizedReviewAndRating(userId: Long) =
        localReviewAndRatingDataSource.getNotSynchronizedReviewsAndRatings(userId)
            .mapNotNull { it.toVo() }

    override suspend fun getReviewAndRatingTimestamp(userId: Long): ReviewAndRatingTimestampVo =
        localReviewAndRatingDataSource.getReviewAndRatingTimestamp(userId).toVo()

    override suspend fun addOrUpdateLocalReviewAndRating(
        reviewAndRating: List<ReviewAndRatingVo>,
        userId: Long
    ) {
        val reviewAndRatingLocalDto =
            reviewAndRating.map { it.toLocalDto() }
        localReviewAndRatingDataSource.addOrUpdateReviewAndRating(
            reviewAndRatingLocalDto,
            userId = userId
        )
    }

    override suspend fun updateReviewAndRatingTimestamp(timestamp: ReviewAndRatingTimestampVo) {
        localReviewAndRatingDataSource.updateReviewAndRatingTimestamp(timestamp.toEntity())
    }
}