package main_models.rating_review

class ReviewAndRatingVo(
    val id: Int,
    val ratingScore: Int,
    val reviewText: String?,
    val bookId: String,
    val bookAuthorId: String,
    val userId: Int,
    val userName: String,
    val likesCount: Int?,
    val dislikesCount: Int?,
    val answersCount: Int?,
    val isApprovedReview: Boolean,
    val isDisapprovedReview: Boolean,
    val timestampOfCreatingScore: Long,
    val timestampOfUpdatingScore: Long,
    val timestampOfCreatingReview: Long,
    val timestampOfUpdatingReview: Long,
    val updatedByDeviceId: String?,
    val bookGenreId: Int,
    val isCreatedManuallyBook: Boolean,
    val bookForAllUsers: Boolean,
)