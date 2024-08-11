package ktor

import HttpAppClient
import HttpConstants.GET_ALL_REVIEWS_AND_RATING_BY_BOOK_ID
import HttpParams
import main_models.rest.base.BaseResponse
import main_models.rest.rating_review.ReviewsAndRatingsResponseContent

class RemoteReviewAndRatingDataSource(private val httpClient: HttpAppClient) {
    suspend fun getAllRemoteReviewsAndRatingsByBookId(bookId: String): BaseResponse<ReviewsAndRatingsResponseContent, String>? =
        httpClient.get(
            url = GET_ALL_REVIEWS_AND_RATING_BY_BOOK_ID,
            resultClass = ReviewsAndRatingsResponseContent::class,
            errorClass = String::class,
            params = mapOf(HttpParams.BOOK_ID to bookId)
        )
}