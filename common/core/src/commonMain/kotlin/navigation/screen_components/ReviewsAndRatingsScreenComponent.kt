package navigation.screen_components

import com.arkivanov.decompose.ComponentContext
import main_models.rating_review.ReviewAndRatingVo

interface ReviewsAndRatingsScreenComponent {
    val reviews: List<ReviewAndRatingVo>
    val scrollToReviewId: Int?
    fun onBack()
}

class DefaultReviewsAndRatingsScreenComponent(
    componentContext: ComponentContext,
    override val reviews: List<ReviewAndRatingVo>,
    override val scrollToReviewId: Int?,
    val onBackListener: () -> Unit,
) : ReviewsAndRatingsScreenComponent, ComponentContext by componentContext {

    override fun onBack() {
        onBackListener()
    }
}