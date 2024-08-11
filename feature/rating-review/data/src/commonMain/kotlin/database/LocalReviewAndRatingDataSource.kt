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

    suspend fun getReviewAndRatingTimestamp(userId: Long) =
        reviewAndRatingTimestampDao.getTimestamp(userId).firstOrNull() ?: createEmptyTimestamp(
            userId
        )

    suspend fun updateReviewAndRatingTimestamp(timestamp: ReviewAndRatingTimestampEntity) {
        reviewAndRatingTimestampDao.insertOrUpdateTimestamp(timestamp)
    }

    suspend fun getNotSynchronizedReviewsAndRatings(userId: Long): List<ReviewAndRatingEntity> {
        val timestamp = getReviewAndRatingTimestamp(userId)
        return reviewAndRatingDao.getNotSynchronizedReviewAndRating(
            ratingTimestamp = timestamp.thisDeviceTimestampRating,
            reviewTimestamp = timestamp.thisDeviceTimestampReview,
            userId = userId
        )
    }

    fun getCurrentUserReviewAndRatingByBook(bookId: String, userId: Long): Flow<List<ReviewAndRatingEntity>> =
        reviewAndRatingDao.getCurrentUserReviewAndRatingByBook(bookId, userId)


    private suspend fun createEmptyTimestamp(userId: Long): ReviewAndRatingTimestampEntity {
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

    suspend fun addOrUpdateReviewAndRating(
        reviewAndRating: List<ReviewAndRatingEntity>,
        userId: Long
    ) {
        reviewAndRating.forEach { item ->
            val existedReviewAndRating = reviewAndRatingDao.getReviewAndRatingByBookId(
                item.bookId,
                userId = userId
            ).firstOrNull()
            if (existedReviewAndRating == null) {
                reviewAndRatingDao.insertOrUpdateReviewAndRating(item)
            } else {
                reviewAndRatingDao.insertOrUpdateReviewAndRating(item.copy(localId = existedReviewAndRating.localId))
            }
        }
    }

}