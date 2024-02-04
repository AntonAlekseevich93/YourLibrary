import androidx.compose.ui.text.input.TextFieldValue
import book_editor.BookEditorEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import main_models.AuthorVo
import main_models.BookItemVo
import models.BookCreatorUiState

class BookCreatorViewModel(
    private val platformInfo: PlatformInfo,
    private val interactor: BookCreatorInteractor,
) : BaseEventScope<BaseEvent> {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var parsingJob: Job? = null
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookCreatorUiState> =
        MutableStateFlow(BookCreatorUiState())
    val uiState = _uiState.asStateFlow()

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is BookEditorEvents.OnAuthorTextChanged -> onAuthorTextChanged(
                event.textFieldValue,
                event.textWasChanged
            )
        }
    }

    fun startParseBook(url: String) {
        parsingJob?.cancel()
        parsingJob = scope.launch() {
            _uiState.value.apply {
                startParsing()
                val response = interactor.parseBookUrl(url)
                delay(3000)
                if (this@launch.isActive) {
                    if (response.bookError != null) {
                        setParsingError()
                    } else if (response.bookItem != null) {
                        bookItem.value = response.bookItem
                        needUpdateBookInfo.value = true
                        setParsingSuccess()
                        splitAuthorsNameAndSearch(response.bookItem!!.authorName)
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
        searchJob?.cancel()
        if (authorName.length >= 2) {
            searchJob = scope.launch {
                val response = interactor.searchInAuthorsNameWithRelates(authorName)
                if (response.isNotEmpty()) {
                    _uiState.value.addSimilarAuthors(response)
                } else {
                    clearSearchAuthor()
                }
            }
        } else {
            clearSearchAuthor()
        }
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

    fun createBook(bookItemVoOrNull: BookItemVo?, needCreateAuthor: Boolean) {
        if (bookItemVoOrNull != null) {
            scope.launch {
                val authorId = if (needCreateAuthor) {
                    val newAuthor = createNewAuthor(authorName = bookItemVoOrNull.authorName)
                    interactor.createAuthor(newAuthor)
                    newAuthor.id
                } else {
                    _uiState.value.selectedAuthor.value!!.id
                }
                interactor.createBook(bookItemVoOrNull.copy(authorId = authorId))
            }
        }
    }

    fun getCurrentTimeInMillis(): Long = platformInfo.getCurrentTime().timeInMillis
    fun setSelectedAuthor(authorVo: AuthorVo) {
        _uiState.value.setSelectedAuthor(authorVo)
    }

    private fun splitAuthorsNameAndSearch(authorName: String) {
        searchJob?.cancel()
        val resultSet = mutableSetOf<AuthorVo>()
        searchJob = scope.launch {
            authorName.split(" ").forEach { searchName ->
                if (searchName.length >= 3) {
                    val response = interactor.searchInAuthorsNameWithRelates(searchName)
                    if (response.isNotEmpty()) {
                        resultSet.addAll(response)
                        setSelectedAuthorIfExist(authorName = authorName, similarAuthors = response)
                    }
                }
            }

            if (resultSet.isNotEmpty()) {
                _uiState.value.addSimilarAuthors(similarAuthors = resultSet.toList())
            } else {
                clearSearchAuthor()
            }
        }
    }

    private fun setSelectedAuthorIfExist(authorName: String, similarAuthors: List<AuthorVo>) {
        similarAuthors.forEach { authorItem ->
            if (authorItem.name.equals(authorName, ignoreCase = true)) {
                _uiState.value.setSelectedAuthor(authorItem)
                _uiState.value.authorWasSelectedProgrammatically.value.invoke()
                return@forEach
            } else {
                authorItem.relatedAuthors.forEach {
                    if (it.name.equals(authorName, ignoreCase = true)) {
                        _uiState.value.setSelectedAuthor(authorItem)
                        _uiState.value.authorWasSelectedProgrammatically.value.invoke()
                        return@forEach
                    }
                }
            }
        }
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

    private fun onAuthorTextChanged(textFieldValue: TextFieldValue, textWasChanged: Boolean) {
        _uiState.value.apply {
            if (selectedAuthor.value != null && bookValues.value.isSelectedAuthorNameWasChanged()) {
                clearSelectedAuthor()
            }

            if (textFieldValue.text.isEmpty()) {
                clearSearchAuthor()
            } else if (textWasChanged) {
                searchAuthor(textFieldValue.text)
            }
        }
    }
}