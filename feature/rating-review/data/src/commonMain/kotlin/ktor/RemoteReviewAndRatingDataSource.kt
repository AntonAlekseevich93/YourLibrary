package ktor

import HttpAppClient
import HttpConstants.ADD_OR_UPDATE_RATING_BY_BOOK
import HttpConstants.ADD_OR_UPDATE_REVIEW_BY_BOOK
import HttpConstants.GET_ALL_REVIEWS_AND_RATING_BY_MAIN_BOOK_ID
import HttpParams
import main_models.rest.base.BaseResponse
import main_models.rest.rating_review.RatingRemoteDto
import main_models.rest.rating_review.ReviewRemoteDto
import main_models.rest.rating_review.ReviewsAndRatingsResponseContent

class RemoteReviewAndRatingDataSource(private val httpClient: HttpAppClient) {
    suspend fun getAllRemoteReviewsAndRatingsByBookId(mainBookId: String): BaseResponse<ReviewsAndRatingsResponseContent, String>? =
        httpClient.get(
            url = GET_ALL_REVIEWS_AND_RATING_BY_MAIN_BOOK_ID,
            resultClass = ReviewsAndRatingsResponseContent::class,
            errorClass = String::class,
            params = mapOf(HttpParams.MAIN_BOOK_ID to mainBookId)
        )

    suspend fun addOrUpdateRating(ratingRemoteDto: RatingRemoteDto): BaseResponse<ReviewsAndRatingsResponseContent, String>? =
        httpClient.post(
            url = ADD_OR_UPDATE_RATING_BY_BOOK,
            bodyRequest = ratingRemoteDto,
            errorClass = String::class,
            resultClass = ReviewsAndRatingsResponseContent::class
        )

    suspend fun addOrUpdateReview(reviewRemoteDto: ReviewRemoteDto): BaseResponse<ReviewsAndRatingsResponseContent, String>? =
        httpClient.post(
            url = ADD_OR_UPDATE_REVIEW_BY_BOOK,
            bodyRequest = reviewRemoteDto,
            errorClass = String::class,
            resultClass = ReviewsAndRatingsResponseContent::class
        )
}