package common_events

import BaseEvent

sealed class ReviewAndRatingEvents : BaseEvent {
    class ChangeBookRating(val newRating: Int) : ReviewAndRatingEvents()
    class AddReview(val reviewText: String) : ReviewAndRatingEvents()
    class TextReviewWasChanged(val newText: String, val mainBookId: String) :
        ReviewAndRatingEvents()

    class GetLastCachedReviewText(val mainBookId: String) : ReviewAndRatingEvents()
}