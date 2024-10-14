package navigation.screen_components

import ReviewAndRatingViewModel
import com.arkivanov.decompose.ComponentContext
import di.Inject
import main_models.rating_review.ReviewAndRatingVo

interface ReviewsAndRatingsScreenComponent {
    val reviews: List<ReviewAndRatingVo>
    val scrollToReviewId: Int?
    fun onBack()

    fun getRatingAndReviewViewModel(): ReviewAndRatingViewModel
}

class DefaultReviewsAndRatingsScreenComponent(
    componentContext: ComponentContext,
    override val reviews: List<ReviewAndRatingVo>,
    override val scrollToReviewId: Int?,
    val onBackListener: () -> Unit,
) : ReviewsAndRatingsScreenComponent, ComponentContext by componentContext {
    val viewModel = Inject.instance<ReviewAndRatingViewModel>()

    override fun getRatingAndReviewViewModel() = viewModel

    override fun onBack() {
        onBackListener()
    }
}