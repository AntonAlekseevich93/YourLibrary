import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.AuthorsUiState
import platform.Platform

class AuthorsViewModel(
    private val platform: Platform,
    private val repository: AuthorsRepository
) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState = MutableStateFlow(
        AuthorsUiState(
            platform = platform,
        )
    )
    val uiState = _uiState.asStateFlow()


}