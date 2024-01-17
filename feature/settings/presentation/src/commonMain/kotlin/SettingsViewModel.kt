import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import models.SettingsUiState
import platform.Platform

class SettingsViewModel(
    private val platform: Platform,
    private val repository: SettingsRepository
) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            platform = platform,
        )
    )
    val uiState = _uiState.asStateFlow()

    fun createAppSettingsFile(
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

    fun updateLibraryNameInFile(path: String, oldName: String, newName: String) {
        scope.launch {
            repository.updateLibraryNameInFile(
                path = path,
                oldName = oldName,
                newName = newName,
            )
        }
    }

    suspend fun getLibraryNameIfExist(path: String): String? =
        repository.getLibraryNameIfExist(path)

}