import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import models.SettingsEvents
import models.SettingsUiState
import platform.Platform

class SettingsViewModel(
    private val platform: Platform,
    private val repository: SettingsRepository,
    private val applicationScope: ApplicationScope
) : BaseMVIViewModel<SettingsUiState, BaseEvent>(SettingsUiState(platform)) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is SettingsEvents.OnOpenAdminPanel -> {
                applicationScope.openAdminPanel()
            }
        }
    }


}