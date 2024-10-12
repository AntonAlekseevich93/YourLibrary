import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import models.SettingsEvents
import models.SettingsUiState
import platform.Platform

class SettingsViewModel(
    private val platform: Platform,
    private val interactor: SettingsInteractor,
    private val applicationScope: ApplicationScope,
) : BaseMVIViewModel<SettingsUiState, BaseEvent>(SettingsUiState(platform)) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is SettingsEvents.ClearAllCache -> {
                scope.launch {
                    interactor.clearAllCache()
                }
            }
        }
    }


}