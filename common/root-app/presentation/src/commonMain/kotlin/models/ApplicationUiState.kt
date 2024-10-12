package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import base.BaseUIState
import main_models.BookVo
import main_models.BooksInfoHeader
import main_models.ReadingStatus
import main_models.path.PathInfoVo

data class ApplicationUiState(
    val searchedBooks: List<BookVo> = emptyList(),
    val isHazeBlurEnabled: MutableState<Boolean> = mutableStateOf(true),
    val userState: MutableState<UserState> = mutableStateOf(UserState.IS_NOT_AUTHORIZED)
) : BaseUIState {
    val pathInfoList: SnapshotStateList<PathInfoVo> =
        mutableStateListOf()
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo())

    private val booksInfoHeaderList: MutableList<BooksInfoHeader> = mutableListOf()
    val booksInfoUiState: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookVo>> =
        initializeBookInfo()

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

    fun removeBookBooksInfoUiState(id: String, bookId: String) {
        val header = booksInfoHeaderList.find { it.id == id }
        booksInfoUiState[header]?.removeAll { it.bookId == bookId }
    }

    private fun initializeBookInfo(): SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookVo>> {
        val map: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookVo>> =
            mutableStateMapOf()
        ReadingStatus.entries.forEachIndexed { index, item ->
            val key = BooksInfoHeader(
                name = item.nameValue,
                id = item.id,
                isShelf = false,
                priorityInList = index
            )
            booksInfoHeaderList.add(key)
            map[key] = mutableStateListOf()
        }
        return map
    }
}

enum class UserState {
    IS_AUTHORIZED,
    IS_NOT_AUTHORIZED
    //    NON_VERIFICATION,
}
