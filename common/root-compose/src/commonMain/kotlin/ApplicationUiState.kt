import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import main_models.path.PathInfoVo

class ApplicationUiState(
    val fullScreenBookInfo: MutableState<Boolean> = mutableStateOf(false),
    val showLeftDrawerState: MutableState<Boolean> = mutableStateOf(false),
    val showRightDrawerState: MutableState<Boolean> = mutableStateOf(false),
    val openLeftDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val openRightDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val closeLeftDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val closeRightDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
) {
    val pathInfoList: SnapshotStateList<PathInfoVo> =
        mutableStateListOf()
    private val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo())

    fun addPathInfo(pathInfo: PathInfoVo) {
        if (pathInfo.isSelected) {
            selectedPathInfo.value = pathInfo
        }
        val oldItem = pathInfoList.firstOrNull { it.id == pathInfo.id }
        if (oldItem != null) {
            val index = pathInfoList.indexOf(oldItem)
            pathInfoList.remove(oldItem)
            pathInfoList.add(index, pathInfo)
        } else {
            pathInfoList.add(pathInfo)
        }
    }
}
