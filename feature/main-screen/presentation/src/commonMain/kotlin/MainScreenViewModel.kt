import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.BookItemVo
import main_models.ViewsType
import models.MainScreenUiState

class MainScreenViewModel(private val repository: MainScreenRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()
    val booksMap: MutableMap<String, BookItemVo> = mutableMapOf()

    fun getSelectedPathInfo() {
        scope.launch {
            launch {
                repository.getSelectedPathInfo().collect { pathInfo ->
                    pathInfo?.let {
                        withContext(Dispatchers.Main) {
                            _uiState.value.selectedPathInfo.value = it
                        }
                    }
                }
            }
            launch {
                getAllBooks()
            }
        }
    }

    private suspend fun getAllBooks() {
        repository.getAllBooks().collect { books ->
            val unique = books.subtract(booksMap.values)
            unique.forEach { book ->
                _uiState.value.addBookToBooksInfo(book)
                booksMap[book.id] = book
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

    fun changedReadingStatus(oldStatusId: String, bookId: String) {
        _uiState.value.removeBookBooksInfoUiState(id = oldStatusId, bookId = bookId)
    }

    fun checkIfNeedUpdateBookItem(oldItem: BookItemVo, newItem: BookItemVo) {
        if (oldItem.bookName != newItem.bookName) {
            _uiState.value.removeBookBooksInfoUiState(id = newItem.statusId, bookId = newItem.id)
        }
    }

}