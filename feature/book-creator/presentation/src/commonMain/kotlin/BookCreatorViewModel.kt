import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import main_models.BookItemVo
import main_models.ReadingStatus
import main_models.ShelfVo
import models.BookCreatorUiState

class BookCreatorViewModel(private val repository: BookCreatorRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var parsingJob: Job? = null
    private var searchJob: Job? = null
    private val shelvesWithoutBooks: MutableList<ShelfVo> = mutableListOf()
    private val _uiState: MutableStateFlow<BookCreatorUiState> =
        MutableStateFlow(BookCreatorUiState())
    val uiState = _uiState.asStateFlow()

    init {
        scope.launch {
            repository.getShelvesWithoutBooks().collect {
                shelvesWithoutBooks.addAll(it)
            }
        }
    }

    fun startParseBook(url: String) {
        parsingJob?.cancel()
        parsingJob = scope.launch() {
            _uiState.value.apply {
                startParsing()
                val response = repository.parseBookUrl(url)
                delay(3000)
                if (this@launch.isActive) {
                    if (response.bookError != null) {
                        setParsingError()
                    } else if (response.bookItem != null) {
                        bookItem.value = response.bookItem
                        needUpdateBookInfo.value = true
                        setParsingSuccess()
                    }
                }
            }
        }
    }

    fun stopParsingBook() {
        parsingJob?.cancel()
        _uiState.value.hideLoadingIndicator()
    }

    fun searchAuthor(authorName: String) {
        //todo реализовать поиск по авторам
//        val list = list.filter { it.contains(authorName, ignoreCase = true) }
//        _uiState.value.addSimilarAuthor(list)
    }

    fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }

    fun hideLoadingStatusIndicator() {
        _uiState.value.hideLoadingIndicator()
    }

    fun clearAllBookData() {
        _uiState.value.clearAllBookData()
    }

    fun createBook(bookItemVoOrNull: BookItemVo?) {
        if (bookItemVoOrNull != null) {
            scope.launch {
                repository.createBook(bookItemVoOrNull)
            }
        }
    }

    fun getShelfIdOrNullByReadingStatus(status: ReadingStatus): String? {
        shelvesWithoutBooks.find { it.name == status.nameValue }?.let {
            return it.id
        }
        return null
    }
}