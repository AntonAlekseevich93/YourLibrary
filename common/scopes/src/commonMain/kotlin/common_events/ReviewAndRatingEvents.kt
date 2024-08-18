package common_events

import BaseEvent

sealed class ReviewAndRatingEvents : BaseEvent {
    class ChangeBookRating(val newRating: Int): ReviewAndRatingEvents()
    class AddReview(val reviewText: String): ReviewAndRatingEvents()
}