import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import models.ReviewAndRatingUiState
import platform.Platform

class ReviewAndRatingViewModel(
    private val platform: Platform,
    private val interactor: ReviewAndRatingInteractor,
    private val appConfig: AppConfig,
) : BaseMVIViewModel<ReviewAndRatingUiState, BaseEvent>(ReviewAndRatingUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    override fun sendEvent(event: BaseEvent) {
        when (event) {

        }
    }


}