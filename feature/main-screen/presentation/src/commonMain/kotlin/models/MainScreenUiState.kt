package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import main_models.BookItemVo
import main_models.BooksInfoHeader
import main_models.ReadingStatus
import main_models.ViewsType
import main_models.path.PathInfoVo

class MainScreenUiState(
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo()),
    val viewsTypes: ViewTypesUiState = ViewTypesUiState(),
) {
    private val booksInfoHeaderList: MutableList<BooksInfoHeader> = mutableListOf()
    val booksInfoUiState: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookItemVo>> =
        initializeBookInfo()

    fun addBookToBooksInfo(book: BookItemVo) {
        booksInfoHeaderList.forEach { header ->
            if (!header.isShelf) {
                if (book.statusId == header.id) {
                    booksInfoUiState[header]?.add(book)
                }
            } else if (book.shelfId == header.id) {
                booksInfoUiState[header]?.add(book)
            }
        }
    }

    fun removeBookBooksInfoUiState(id: String, bookId: String) {
        val header = booksInfoHeaderList.find { it.id == id }
        booksInfoUiState[header]?.removeAll { it.id == bookId }
    }

    private fun initializeBookInfo(): SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookItemVo>> {
        val map: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookItemVo>> =
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

data class ViewTypesUiState(
    private val _selectedViewTypes: SnapshotStateList<ViewsType> =
        mutableStateListOf(ViewsType.KANBAN, ViewsType.LIST, ViewsType.CALENDAR),
    val selectedViewTypes: SnapshotStateList<ViewsType> = _selectedViewTypes,
    private val _checkedViewTypes: SnapshotStateList<ViewsType> =
        mutableStateListOf(),
    val checkedViewTypes: SnapshotStateList<ViewsType> = _checkedViewTypes,
    val openedViewType: MutableState<ViewsType> = mutableStateOf(ViewsType.KANBAN)
) {

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