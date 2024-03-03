import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import models.SettingsDataProvider
import models.SettingsEvents
import models.SettingsUiState
import platform.Platform

class SettingsViewModel(
    private val platform: Platform,
    private val repository: SettingsRepository,
    private val navigationHandler: NavigationHandler,
) : BaseMVIViewModel<SettingsUiState, BaseEvent>(SettingsUiState(platform)), SettingsDataProvider {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is SettingsEvents.CloseSettingsScreen -> navigationHandler.goBack()
        }
    }

    override fun createAppSettingsFile(
        path: String,
        libraryName: String,
        themeName: String,
    ) {
        scope.launch {
            repository.createAppSettingsFile(
                path = path,
                libraryName = libraryName,
                themeName = themeName
            )
        }
    }

    override fun updateLibraryNameInFile(path: String, oldName: String, newName: String) {
        scope.launch {
            repository.updateLibraryNameInFile(
                path = path,
                oldName = oldName,
                newName = newName,
            )
        }
    }

    override suspend fun getLibraryNameIfExist(path: String): String? =
        repository.getLibraryNameIfExist(path)

}