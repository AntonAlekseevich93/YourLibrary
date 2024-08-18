import database.LocalReviewAndRatingDataSource
import database.room.entities.toEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteReviewAndRatingDataSource
import main_models.rating_review.ReviewAndRatingTimestampVo
import main_models.rating_review.ReviewAndRatingVo
import main_models.rest.rating_review.toRemoteRating
import main_models.rest.rating_review.toRemoteReview
import main_models.rest.rating_review.toVo
import platform.PlatformInfoData

class ReviewAndRatingRepositoryImpl(
    private val localReviewAndRatingDataSource: LocalReviewAndRatingDataSource,
    private val remoteReviewAndRatingDataSource: RemoteReviewAndRatingDataSource,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
    private val platformInfo: PlatformInfoData,
) : ReviewAndRatingRepository {

    override suspend fun getNotSynchronizedReviewAndRating(userId: Long) =
        localReviewAndRatingDataSource.getNotSynchronizedReviewsAndRatings(userId)
            .mapNotNull { it.toVo() }

    override suspend fun getReviewAndRatingTimestamp(userId: Long): ReviewAndRatingTimestampVo =
        localReviewAndRatingDataSource.getReviewAndRatingTimestamp(userId).toVo()

    override suspend fun addOrUpdateLocalReviewAndRatingWhenSync(
        reviewAndRating: List<ReviewAndRatingVo>,
        userId: Long
    ) {
        val reviewAndRatingLocalDto =
            reviewAndRating.map { it.toLocalDto() }
        localReviewAndRatingDataSource.addOrUpdateLocalReviewAndRatingWhenSync(
            reviewAndRatingLocalDto,
            userId = userId
        )
    }

    override suspend fun addOrUpdateRatingByBookId(
        newRating: Int,
        bookId: String,
        bookAuthorId: String,
        bookGenreId: Int,
        isCreatedManuallyBook: Boolean,
        bookForAllUsers: Boolean,
    ) {
        val userId = appConfig.userId
        val existedRating = localReviewAndRatingDataSource.getCurrentUserReviewAndRatingByBook(
            bookId = bookId,
            userId = userId
        ).firstOrNull()
        val currentLocalTimestamp =
            localReviewAndRatingDataSource.getReviewAndRatingTimestamp(userId)
        var resultReviewAndRatingVo: ReviewAndRatingVo? = null
        val timestamp = platformInfo.getCurrentTime().timeInMillis
        if (existedRating == null) {
            resultReviewAndRatingVo = ReviewAndRatingVo.createEmptyRating(
                rating = newRating,
                bookId = bookId,
                bookAuthorId = bookAuthorId,
                bookGenreId = bookGenreId,
                isCreatedManuallyBook = isCreatedManuallyBook,
                bookForAllUsers = bookForAllUsers,
                userId = userId.toInt(),
                userName = "Антон Алексеевич", //todo fix this
                deviceId = appConfig.deviceId,
                timestamp = timestamp
            )
        } else {
            resultReviewAndRatingVo = existedRating.copy(
                ratingScore = newRating,
                timestampOfUpdatingScore = timestamp
            ).toVo()
        }

        val id = localReviewAndRatingDataSource.addOrUpdateJustRating(
            resultReviewAndRatingVo.toLocalDto(),
            userId = userId
        )

        remoteReviewAndRatingDataSource.addOrUpdateRating(
            ratingRemoteDto = resultReviewAndRatingVo.toRemoteRating()
        )?.result?.reviewAndRating?.let { response ->
            response.toVo()?.toLocalDto()?.let { localRating ->
                localReviewAndRatingDataSource.addOrUpdateJustRating(
                    reviewAndRating = localRating.copy(localId = id),
                    userId = userId
                )
                localReviewAndRatingDataSource.updateReviewAndRatingTimestamp(
                    currentLocalTimestamp.copy(
                        thisDeviceTimestampRating = localRating.timestampOfUpdatingScore
                    )
                )
            }
        }
    }

    override suspend fun addReviewByBookId(
        reviewText: String,
        bookId: String,
    ) {
        val userId = appConfig.userId
        val existedRating = localReviewAndRatingDataSource.getCurrentUserReviewAndRatingByBook(
            bookId = bookId,
            userId = userId
        ).firstOrNull()
        val currentLocalTimestamp =
            localReviewAndRatingDataSource.getReviewAndRatingTimestamp(userId)
        var resultReviewAndRatingVo: ReviewAndRatingVo? = null
        val timestamp = platformInfo.getCurrentTime().timeInMillis
        if (existedRating != null) {
            resultReviewAndRatingVo = existedRating.copy(
                reviewText = reviewText,
                timestampOfCreatingReview = timestamp,
            ).toVo()

            localReviewAndRatingDataSource.addJustReview(
                resultReviewAndRatingVo.toLocalDto(),
                userId = userId
            )?.let { localId ->
                resultReviewAndRatingVo.toRemoteReview()?.let { remoteDto ->
                    remoteReviewAndRatingDataSource.addOrUpdateReview(
                        reviewRemoteDto = remoteDto
                    )?.result?.reviewAndRating?.let { response ->
                        response.toVo()?.toLocalDto()?.let { localRating ->
                            localReviewAndRatingDataSource.updateJustReview(
                                reviewAndRating = localRating.copy(localId = localId),
                                userId = userId
                            )
                            localReviewAndRatingDataSource.updateReviewAndRatingTimestamp(
                                currentLocalTimestamp.copy(
                                    thisDeviceTimestampRating = localRating.timestampOfUpdatingReview
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateReviewAndRatingTimestamp(timestamp: ReviewAndRatingTimestampVo) {
        localReviewAndRatingDataSource.updateReviewAndRatingTimestamp(timestamp.toEntity())
    }

    override suspend fun getCurrentUserLocalReviewAndRatingByBook(bookId: String): Flow<ReviewAndRatingVo?> =
        localReviewAndRatingDataSource.getCurrentUserReviewAndRatingByBookFlow(
            bookId = bookId,
            userId = appConfig.userId
        ).map { list ->
            list.firstOrNull()?.toVo()
        }

    override suspend fun getAllRemoteReviewsAndRatingsByBookId(bookId: String): List<ReviewAndRatingVo> =
        remoteReviewAndRatingDataSource.getAllRemoteReviewsAndRatingsByBookId(bookId)?.result?.reviewsAndRatings?.mapNotNull { it.toVo() }
            ?: emptyList()

}