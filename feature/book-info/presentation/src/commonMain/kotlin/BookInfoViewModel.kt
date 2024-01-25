import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.ReadingStatus
import models.BookInfoUiState

class BookInfoViewModel(
    private val platformInfo: PlatformInfo,
    private val interactor: BookInfoInteractor,
) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookInfoUiState> = MutableStateFlow(BookInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        scope.launch {
            interactor.getSelectedPathInfo().collect { pathInfo ->
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
            interactor.getBookById(bookId = id).collect { book ->
                withContext(Dispatchers.Main) {
                    _uiState.value.setBookItem(book)
                }
                launch {
                    interactor.getAuthorWithRelatesWithoutBooks(book.authorId)?.let {
                        _uiState.value.setSelectedAuthor(it)
                        _uiState.value.authorWasSelectedProgrammatically.value.invoke()
                    }
                }
            }
        }
    }

    fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }

    fun updateBook(bookItem: BookItemVo, needCreateNewAuthor: Boolean) {
        scope.launch {
            if (needCreateNewAuthor) {
                val author = createNewAuthor(authorName = bookItem.authorName)
                interactor.createAuthor(author)
                interactor.updateBook(bookItem.copy(authorId = author.id))
            } else {
                interactor.updateBook(bookItem)
            }
        }
    }

    fun changeReadingStatus(status: ReadingStatus, bookId: String) {
        scope.launch {
            interactor.changeBookStatusId(status, bookId)
        }
    }

    fun getCurrentTimeInMillis(): Long = platformInfo.getCurrentTime().timeInMillis

    fun setSelectedAuthor(author: AuthorVo) {
        _uiState.value.setSelectedAuthor(author)
    }

    fun searchAuthor(authorName: String) {
        searchJob?.cancel()
        if (authorName.length >= 2) {
            searchJob = scope.launch {
                val result = interactor.searchInAuthorsNameWithRelates(authorName)
                if (result.isNotEmpty()) {
                    _uiState.value.addSimilarAuthors(result)
                } else {
                    clearSearchAuthor()
                }
            }
        } else {
            clearSearchAuthor()
        }
    }

    fun clearSelectedAuthor() {
        _uiState.value.clearSelectedAuthor()
    }

    private fun createNewAuthor(
        authorName: String
    ): AuthorVo {
        _uiState.value.apply {
            return AuthorVo(
                id = AuthorVo.generateId(),
                name = authorName,
                isMainAuthor = true,
                timestampOfCreating = platformInfo.getCurrentTime().timeInMillis,
                timestampOfUpdating = platformInfo.getCurrentTime().timeInMillis,
                relatedToAuthorId = null,
                books = emptyList()
            )
        }
    }
}