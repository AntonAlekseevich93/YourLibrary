import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.ViewsType
import models.MainScreenUiState

class MainScreenViewModel(private val repository: MainScreenRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun getSelectedPathInfo() {
        scope.launch {
            repository.getSelectedPathInfo().collect { pathInfo ->
                pathInfo?.let {
                    withContext(Dispatchers.Main) {
                        _uiState.value.selectedPathInfo.value = it
                    }
                }
            }
        }
    }

    fun switchViewTypesListener(isChecked: Boolean, viewsType: ViewsType) {
        _uiState.value.viewsTypes.changeViewTypes(isChecked, viewsType)
    }

    fun changeViewsTypes() {
        _uiState.value.viewsTypes.applyCheckedViewTypes()
        //todo здесь изменить экран который открыт если этого item больше нет
    }

    fun openViewType(viewsType: ViewsType) {
        _uiState.value.viewsTypes.openedViewType.value = viewsType
    }

}