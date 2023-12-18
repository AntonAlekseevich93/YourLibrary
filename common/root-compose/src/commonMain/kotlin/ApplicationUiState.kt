import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import sub_app_bar.ViewsType

class ApplicationUiState() {
    private val _selectedViewTypes: SnapshotStateList<ViewsType> =
        mutableStateListOf<ViewsType>(ViewsType.KANBAN, ViewsType.LIST, ViewsType.CALENDAR)
    val selectedViewTypes: SnapshotStateList<ViewsType> = _selectedViewTypes

    private val _checkedViewTypes: SnapshotStateList<ViewsType> =
        mutableStateListOf<ViewsType>()
    val checkedViewTypes: SnapshotStateList<ViewsType> = _checkedViewTypes

    val openedViewType: MutableState<ViewsType> = mutableStateOf(ViewsType.KANBAN)

    init {
        _checkedViewTypes.addAll(_selectedViewTypes)
    }

    fun changeViewTypes(isChecked: Boolean, viewsType: ViewsType) {
        if (isChecked) {
            _checkedViewTypes.add(viewsType)
        } else {
            _checkedViewTypes.remove(viewsType)
        }
    }

    fun applyCheckedViewTypes() {
        val items = _checkedViewTypes
        if (!items.contains(openedViewType.value) && items.isNotEmpty()) {
            openedViewType.value = _checkedViewTypes.first()
        }
        _selectedViewTypes.clear()
        _selectedViewTypes.addAll(items)
    }
}
