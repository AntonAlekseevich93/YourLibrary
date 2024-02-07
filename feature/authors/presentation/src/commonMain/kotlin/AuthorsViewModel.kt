import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import models.AuthorsUiState
import platform.Platform

class AuthorsViewModel(
    private val platform: Platform,
    private val interactor: AuthorsInteractor,
) : BaseMVIViewModel<AuthorsUiState, BaseEvent>(AuthorsUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    init {
        getAllAuthors()
    }

    override fun sendEvent(event: BaseEvent) {

    }

    private fun getAllAuthors() {
        scope.launch {
            val authors = interactor.getAllAuthorsByAlphabet()
            updateUIState(
                uiStateValue.copy(
                    authorByAlphabet = authors
                )
            )
        }
    }

}