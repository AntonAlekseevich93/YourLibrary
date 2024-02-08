import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource
import main_models.BookItemVo
import main_models.BooksInfoHeader
import main_models.ReadingStatus
import main_models.path.PathInfoVo

class ApplicationUiState(
    val fullScreenBookInfo: MutableState<Boolean> = mutableStateOf(false),
    val showLeftDrawerState: MutableState<Boolean> = mutableStateOf(false),
    val showRightDrawerState: MutableState<Boolean> = mutableStateOf(false),
    val openLeftDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val openRightDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val closeLeftDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val closeRightDrawerEvent: MutableState<() -> Unit> = mutableStateOf({}),
    val painterSelectedBookInCache: MutableState<Resource<Painter>?> = mutableStateOf(null),
    val selectedBookId: MutableState<String> = mutableStateOf(""),
) {
    val pathInfoList: SnapshotStateList<PathInfoVo> =
        mutableStateListOf()
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo())

    private val booksInfoHeaderList: MutableList<BooksInfoHeader> = mutableListOf()
    val booksInfoUiState: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookItemVo>> =
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