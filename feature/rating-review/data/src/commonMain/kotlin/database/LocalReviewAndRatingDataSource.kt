package database

import database.room.RoomMainDataSource
import database.room.entities.ReviewAndRatingEntity
import database.room.entities.ReviewAndRatingTimestampEntity
import kotlinx.coroutines.flow.Flow
import platform.PlatformInfoData

class LocalReviewAndRatingDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val reviewAndRatingDao = roomDb.reviewAndRatingDao
    private val reviewAndRatingTimestampDao = roomDb.reviewAndRatingTimestampDao

    suspend fun getReviewAndRatingTimestamp(userId: Int) =
        reviewAndRatingTimestampDao.getTimestamp(userId).firstOrNull() ?: createEmptyTimestamp(
            userId
        )

    suspend fun updateReviewAndRatingTimestamp(timestamp: ReviewAndRatingTimestampEntity) {
        reviewAndRatingTimestampDao.insertOrUpdateTimestamp(timestamp)
    }

    suspend fun getNotSynchronizedReviewsAndRatings(userId: Int): List<ReviewAndRatingEntity> {
        val timestamp = getReviewAndRatingTimestamp(userId)
        return reviewAndRatingDao.getNotSynchronizedReviewAndRating(
            ratingTimestamp = timestamp.thisDeviceTimestampRating,
            reviewTimestamp = timestamp.thisDeviceTimestampReview,
            userId = userId
        )
    }

    suspend fun getCurrentUserReviewAndRatingByBookFlow(
        mainBookId: String,
        userId: Int
    ): Flow<List<ReviewAndRatingEntity>> =
        reviewAndRatingDao.getCurrentUserReviewAndRatingByBookFlow(mainBookId, userId)

    suspend fun getCurrentUserReviewAndRatingByBook(
        mainBookId: String,
        userId: Int
    ): List<ReviewAndRatingEntity> =
        reviewAndRatingDao.getCurrentUserReviewAndRatingByBook(mainBookId, userId)

    private suspend fun createEmptyTimestamp(userId: Int): ReviewAndRatingTimestampEntity {
        val timestamp = ReviewAndRatingTimestampEntity(
            userId = userId,
            otherDevicesTimestampRating = 0,
            thisDeviceTimestampRating = 0,
            otherDevicesTimestampReview = 0,
            thisDeviceTimestampReview = 0,
        )
        reviewAndRatingTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }

    /**do not use this function except for synchronization**/
    suspend fun addOrUpdateLocalReviewAndRatingWhenSync(
        reviewAndRating: List<ReviewAndRatingEntity>,
        userId: Int
    ) {
        reviewAndRating.forEach { item ->
            val existedReviewAndRating = reviewAndRatingDao.getReviewAndRatingByBookId(
                mainBookId = item.mainBookId,
                userId = userId
            ).firstOrNull()
            if (existedReviewAndRating == null) {
                reviewAndRatingDao.insertOrUpdateReviewAndRating(item)
            } else {
                reviewAndRatingDao.insertOrUpdateReviewAndRating(item.copy(localId = existedReviewAndRating.localId))
            }
        }
    }

    suspend fun addOrUpdateJustRating(
        reviewAndRating: ReviewAndRatingEntity,
        userId: Int,
    ): Long {
        val existedReviewAndRating = reviewAndRatingDao.getReviewAndRatingByBookId(
            mainBookId = reviewAndRating.mainBookId,
            userId = userId
        ).firstOrNull()
        return if (existedReviewAndRating == null) {
            reviewAndRatingDao.insertOrUpdateReviewAndRating(reviewAndRating)
        } else {
            reviewAndRatingDao.insertOrUpdateReviewAndRating(
                existedReviewAndRating.copy(
                    ratingScore = reviewAndRating.ratingScore,
                    timestampOfUpdatingScore = reviewAndRating.timestampOfUpdatingScore
                )
            )
        }
    }

    suspend fun addJustReview(
        reviewAndRating: ReviewAndRatingEntity,
        userId: Int,
    ): Long? {
        if (reviewAndRating.reviewText.isNullOrEmpty()) return null
        val existedReviewAndRating = reviewAndRatingDao.getReviewAndRatingByBookId(
            mainBookId = reviewAndRating.mainBookId,
            userId = userId
        ).firstOrNull()

        existedReviewAndRating?.copy(
            reviewText = reviewAndRating.reviewText,
            timestampOfCreatingReview = reviewAndRating.timestampOfCreatingReview,
        )?.let {
            return reviewAndRatingDao.insertOrUpdateReviewAndRating(it)
        }
        return null
    }

    suspend fun updateJustReview(
        reviewAndRating: ReviewAndRatingEntity,
        userId: Int,
    ): Long? {
        if (reviewAndRating.reviewText.isNullOrEmpty()) return null
        val existedReviewAndRating = reviewAndRatingDao.getReviewAndRatingByBookId(
            mainBookId = reviewAndRating.mainBookId,
            userId = userId
        ).firstOrNull()
        existedReviewAndRating?.copy(
            reviewText = reviewAndRating.reviewText,
            timestampOfUpdatingReview = reviewAndRating.timestampOfUpdatingReview,
        )?.let {
            return reviewAndRatingDao.insertOrUpdateReviewAndRating(it)
        }
        return null
    }

    suspend fun getCurrentUserAllReviewsAndRating(userId: Int) =
        reviewAndRatingDao.getCurrentUserAllReviewsAndRatings(userId)
}
