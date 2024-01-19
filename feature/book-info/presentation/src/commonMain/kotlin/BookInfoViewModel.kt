import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.BookItemVo
import main_models.ReadingStatus
import models.BookInfoUiState

class BookInfoViewModel(private val repository: BookInfoRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookInfoUiState> = MutableStateFlow(BookInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
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

    fun getBookItem(id: String) {
        scope.launch {
            repository.getBookById(bookId = id).collect { book ->
                withContext(Dispatchers.Main) {
                    _uiState.value.setBookItem(book)
                }
            }
        }
    }

    fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }

    fun updateBook(bookItem: BookItemVo) {
        scope.launch {
            repository.updateBook(bookItem)
        }
    }

    fun changeReadingStatus(status: ReadingStatus, bookId: String) {
        scope.launch {
            repository.changeBookStatusId(status, bookId)
        }
    }

}