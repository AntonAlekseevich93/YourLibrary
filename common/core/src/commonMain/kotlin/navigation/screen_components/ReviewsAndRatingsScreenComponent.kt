package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ReviewsAndRatingsScreenComponent {
    fun onBack()
}

class DefaultReviewsAndRatingsScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : ReviewsAndRatingsScreenComponent, ComponentContext by componentContext {

    override fun onBack() {
        onBackListener()
    }
}