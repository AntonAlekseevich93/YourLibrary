import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import sub_app_bar.ViewsType

class ApplicationViewModel {
    private val _uiState = MutableStateFlow(ApplicationUiState())
    val uiState: StateFlow<ApplicationUiState> = _uiState

    fun switchViewTypesListener(isChecked: Boolean, viewsType: ViewsType) {
        _uiState.value.changeViewTypes(isChecked, viewsType)
    }

    fun changeViewsTypes() {
        _uiState.value.applyCheckedViewTypes()
        //todo здесь изменить экран который открыт если этого item больше нет
    }

    fun openViewType(viewsType: ViewsType) {
        _uiState.value.openedViewType.value = viewsType
    }
}