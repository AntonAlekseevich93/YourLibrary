import androidx.compose.ui.text.input.TextFieldValue
import book_editor.BookEditorEvents
import date.DatePickerEvents
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
import main_models.DatePickerType
import models.BookCreatorEvents
import models.BookCreatorUiState
import text_fields.DELAY_FOR_LISTENER_PROCESSING

class BookCreatorViewModel(
    private val platformInfo: PlatformInfo,
    private val interactor: BookCreatorInteractor,
    private val navigationHandler: NavigationHandler,
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

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)
            is BookCreatorEvents.GoBack -> navigationHandler.goBack()
            is BookCreatorEvents.CreateBookEvent -> createBook()
            is BookCreatorEvents.UrlTextChangedEvent -> urlTextChanged(event.urlTextFieldValue)
            is BookCreatorEvents.ClearUrlEvent -> clearUrl()
            is DatePickerEvents.OnSelectedDate -> setSelectedDate(event.millis, event.text)
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                _uiState.value.showDatePicker.value = false
            }
        }
    }

    fun hideLoadingStatusIndicator() {
        _uiState.value.hideLoadingIndicator()
    }

    private fun getCurrentTimeInMillis(): Long = platformInfo.getCurrentTime().timeInMillis

    private fun createBook() {
        _uiState.value.apply {
            bookValues.value.createBookItemWithoutAuthorIdOrNull(
                timestampOfCreating = getCurrentTimeInMillis(),
                timestampOfUpdating = getCurrentTimeInMillis(),
            )?.let { bookItem ->
                scope.launch {
                    launch {
                        val authorId = if (needCreateNewAuthor.value) {
                            val newAuthor = createNewAuthor(authorName = bookItem.authorName)
                            interactor.createAuthor(newAuthor)
                            newAuthor.id
                        } else {
                            selectedAuthor.value!!.id
                        }
                        interactor.createBook(bookItem.copy(authorId = authorId))
                    }

                    launch {
                        clearAllAuthorInfo()
                        navigationHandler.goBack()
                    }
                }
            }
        }
    }

    private fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
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

    private fun searchAuthor(authorName: String) {
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

    private fun onSuggestionAuthorClick(author: AuthorVo) {
        _uiState.value.setSelectedAuthor(author)

        scope.launch {
            delay(DELAY_FOR_LISTENER_PROCESSING)
            clearSearchAuthor()
        }
    }

    private fun urlTextChanged(urlTextFieldValue: TextFieldValue) {
        _uiState.value.bookValues.value.parsingUrl.value = urlTextFieldValue
        if (urlTextFieldValue.text.isNotEmpty() && urlTextFieldValue.text.length > 5) {
            startParseBook(url = urlTextFieldValue.text)
        } else {
            stopParsingBook()
        }
    }

    private fun startParseBook(url: String) {
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

    private fun stopParsingBook() {
        parsingJob?.cancel()
        _uiState.value.hideLoadingIndicator()
    }

    private fun clearUrl() {
        _uiState.value.apply {
            urlFieldIsWork.value = true
            showClearButtonOfUrlElement.value = false
            showParsingResult.value = false
            bookValues.value.clearAll()
            clearAllBookData()
            showDialogClearAllData.value = false
        }
    }

    private fun setSelectedDate(millis: Long, text: String) {
        _uiState.value.apply {
            when (datePickerType.value) {
                DatePickerType.StartDate -> {
                    bookValues.value.startDateInMillis.value = millis
                    bookValues.value.startDateInString.value = text
                }

                DatePickerType.EndDate -> {
                    bookValues.value.endDateInMillis.value = millis
                    bookValues.value.endDateInString.value = text
                }
            }
        }
    }

    private fun showDatePicker(type: DatePickerType) {
        _uiState.value.apply {
            datePickerType.value = type
            showDatePicker.value = true
        }
    }

}