import database.SqlDelightDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Platform
import sub_app_bar.ViewsType

class ApplicationViewModel(private val db: SqlDelightDataSource) {
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

    fun dbPathIsExist(platform: Platform): Boolean = db.pathIsExist(platform)
    fun createDbPath(dbPath: String): Boolean = db.createDbPath(dbPath)
}