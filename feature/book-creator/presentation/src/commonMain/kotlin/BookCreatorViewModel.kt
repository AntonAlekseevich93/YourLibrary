import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import base.BaseMVIViewModel
import book_editor.BookEditorEvents
import date.DatePickerEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.BookValues
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.rest.LoadingStatus
import models.BookCreatorEvents
import models.BookCreatorUiState
import text_fields.DELAY_FOR_LISTENER_PROCESSING

class BookCreatorViewModel(
    private val platformInfo: PlatformInfo,
    private val interactor: BookCreatorInteractor,
    private val navigationHandler: NavigationHandler,
) : BaseMVIViewModel<BookCreatorUiState, BaseEvent>(BookCreatorUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var parsingJob: Job? = null
    private var searchJob: Job? = null

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is BookEditorEvents.OnAuthorTextChanged -> onAuthorTextChanged(
                event.textFieldValue,
                event.textWasChanged
            )

            is BookEditorEvents.OnBookNameChanged -> {
                searchBookName(event.bookName)
            }

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)
            is BookEditorEvents.OnBookSelected -> setSelectedBook(event.shortBook)
            is BookEditorEvents.OnChangeNeedCreateNewAuthor -> {
                updateUIState(uiStateValue.copy(needCreateNewAuthor = event.needCreate))
            }

            is BookEditorEvents.OnShowAlertDialog -> {
                updateUIState(
                    uiStateValue.copy(
                        showCommonAlertDialog = true,
                        alertDialogConfig = event.config
                    )
                )
            }

            is BookEditorEvents.OnCreateBookManually -> {
                updateUIState(uiStateValue.copy(isCreateBookManually = true, showSearchAuthorError = false))
            }

            is BookEditorEvents.OnSearchAuthorClick -> {
                searchAuthor(event.name)
            }

            is BookEditorEvents.ClearBookSearch -> {
                uiStateValue.similarBooks.clear()
                updateUIState(uiStateValue.copy(showSearchBookError = false))
            }

            is BookCreatorEvents.GoBack -> navigationHandler.goBack()
            is BookCreatorEvents.CreateBookEvent -> createBook()
            is BookCreatorEvents.UrlTextChangedEvent -> urlTextChanged(event.urlTextFieldValue)
            is BookCreatorEvents.ClearUrlEvent -> clearUrl()
            is BookCreatorEvents.OnClearUrlAndCreateBookManuallyEvent -> {
                uiStateValue.bookValues.parsingUrl.value = TextFieldValue()
                updateUIState(
                    uiStateValue.copy(
                        isCreateBookManually = true,
                        showLoadingIndicator = false
                    )
                )
            }

            is BookCreatorEvents.OnFinishParsingUrl -> finishParsing()
            is BookCreatorEvents.OnCreateBookManuallyEvent -> {
                uiStateValue.bookValues.parsingUrl.value = TextFieldValue()
                updateUIState(
                    uiStateValue.copy(
                        isCreateBookManually = true
                    )
                )
            }

            is BookCreatorEvents.DisableCreateBookManuallyEvent -> {
                updateUIState(
                    uiStateValue.copy(
                        isCreateBookManually = false
                    )
                )
            }

            is BookCreatorEvents.ClearAllAuthorInfo -> {
                clearAllAuthorInfo()
            }

            is BookCreatorEvents.OnShowDialogClearAllData -> {
                updateUIState(uiStateValue.copy(showDialogClearAllData = event.show))
            }

            is BookCreatorEvents.DismissCommonAlertDialog -> {
                updateUIState(
                    uiStateValue.copy(
                        showCommonAlertDialog = false,
                        alertDialogConfig = null
                    )
                )
            }

            is BookCreatorEvents.SetBookCoverManually -> {
                uiStateValue.bookValues.clearCoverUrl()
                updateUIState(
                    uiStateValue.copy(
                        isBookCoverManually = true,
                        showCommonAlertDialog = false,
                        alertDialogConfig = null
                    )
                )
            }

            is DatePickerEvents.OnSelectedDate -> setSelectedDate(event.millis, event.text)
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                updateUIState(
                    uiStateValue.copy(
                        showDatePicker = false
                    )
                )
            }
        }
    }

    private fun clearAllAuthorInfo() {
        updateUIState(
            uiStateValue.copy(
                selectedAuthor = null
            )
        )
        uiStateValue.similarSearchAuthors.clear()
    }

    private fun getCurrentTimeInMillis(): Long = platformInfo.getCurrentTime().timeInMillis

    private fun createBook() {
        uiStateValue.bookValues.createBookItemWithoutAuthorIdOrNull(
            timestampOfCreating = getCurrentTimeInMillis(),
            timestampOfUpdating = getCurrentTimeInMillis(),
        )?.let { bookItem ->
            scope.launch {
                launch {
                    val authorId = if (uiStateValue.needCreateNewAuthor) {
                        val newAuthor =
                            createNewAuthor(authorName = bookItem.originalAuthorName)
                        interactor.createAuthor(newAuthor)
                        newAuthor.id
                    } else {
                        uiStateValue.selectedAuthor!!.id
                    }
                    interactor.createBook(bookItem.copy(originalAuthorId = authorId))
                }

                launch {
                    clearAllAuthorInfo()
                    navigationHandler.goBack()
                }
            }
        }
    }

    private fun clearSearchAuthor(showError: Boolean = false) {
        uiStateValue.similarSearchAuthors.clear()
        updateUIState(
            state = uiStateValue.copy(isSearchAuthorProcess = false, showSearchAuthorError = showError)
        )
    }

    private fun splitAuthorsNameAndSearch(authorName: String) {
        searchJob?.cancel()
//        val resultSet = mutableSetOf<AuthorVo>()
//        searchJob = scope.launch {
//            authorName.split(" ").forEach { searchName ->
//                if (searchName.length >= 3) {
//                    val response = interactor.searchInAuthorsNameWithRelates(searchName)
//                    if (response.isNotEmpty()) {
//                        resultSet.addAll(response)
//                        setSelectedAuthorIfExist(authorName = authorName, similarAuthors = response)
//                    }
//                }
//            }
//
//            if (resultSet.isNotEmpty()) {
//                _uiState.value.addSimilarAuthors(similarAuthors = resultSet.toList())
//            } else {
//                clearSearchAuthor()
//            }
//        }
        //todo добавлен поиск на бэке
    }

    private fun setSelectedAuthorIfExist(authorName: String, similarAuthors: List<AuthorVo>) {
        similarAuthors.forEach { authorItem ->
            if (authorItem.name.equals(authorName, ignoreCase = true)) {
                updateUIState(uiStateValue.copy(selectedAuthor = authorItem))
                setSelectedAuthorName()
                return@forEach
            } else {
                authorItem.relatedAuthors.forEach {
                    if (it.name.equals(authorName, ignoreCase = true)) {
                        updateUIState(uiStateValue.copy(selectedAuthor = authorItem))
                        setSelectedAuthorName()
                        return@forEach
                    }
                }
            }
        }
    }

    private fun setSelectedAuthorName() {
        uiStateValue.selectedAuthor?.let { author ->
            val textPostfix = if (author.relatedAuthors.isNotEmpty()) {
                "(${author.relatedAuthors.joinToString { it.name }})"
            } else ""
            uiStateValue.bookValues.setSelectedAuthorName(
                author.name,
                relatedAuthorsNames = textPostfix
            )
        }
    }


    private fun createNewAuthor(
        authorName: String
    ): AuthorVo {
        return AuthorVo(
            id = AuthorVo.generateId(),
            serverId = null,
            name = authorName,
            uppercaseName = authorName.uppercase(),
            isMainAuthor = true,
            timestampOfCreating = platformInfo.getCurrentTime().timeInMillis,
            timestampOfUpdating = platformInfo.getCurrentTime().timeInMillis,
            relatedToAuthorId = null,
            books = emptyList()
        )
    }

    private fun onAuthorTextChanged(textFieldValue: TextFieldValue, textWasChanged: Boolean) {
        if (uiStateValue.selectedAuthor != null && uiStateValue.bookValues.isSelectedAuthorNameWasChanged()) {
            updateUIState(uiStateValue.copy(selectedAuthor = null))
        }

        if (textFieldValue.text.isEmpty()) {
            clearSearchAuthor()
        }
//        else if (textWasChanged) {
//            searchAuthor(textFieldValue.text)
//        }
    }

    private fun searchAuthor(authorName: String) {
        searchJob?.cancel()
        uiStateValue.similarSearchAuthors.clear()
        updateUIState(uiStateValue.copy(isSearchAuthorProcess = false))
        val uppercaseName = authorName.trim().uppercase()
        if (authorName.length >= 2) {
            updateUIState(uiStateValue.copy(isSearchAuthorProcess = true))
            searchJob = scope.launch {
                delay(500)
                val response = interactor.searchInAuthorsNameWithRelates(uppercaseName)
                if (response.isNotEmpty()) {
                    val list: SnapshotStateList<AuthorVo> = mutableStateListOf()
                    list.addAll(response)
                    val exactMatchAuthor = list.find { it.uppercaseName == uppercaseName }

                    if (exactMatchAuthor == null) {
                        updateUIState(
                            uiStateValue.copy(
                                similarSearchAuthors = list,
                                isSearchAuthorProcess = false
                            )
                        )
                    } else {
                        updateUIState(
                            uiStateValue.copy(
                                similarSearchAuthors = list,
                                selectedAuthor = exactMatchAuthor,
                                isSearchAuthorProcess = false
                            )
                        )
                    }
                } else {
                    clearSearchAuthor(showError = true)
                }
            }
        } else {
            clearSearchAuthor()
        }
    }

    private fun searchBookName(bookName: String) {
        searchJob?.cancel()
        updateUIState(uiStateValue.copy(isSearchBookProcess = false, showSearchBookError = false))
        val uppercaseBookName = bookName.trim().uppercase()
        uiStateValue.similarBooks.clear()
        if (bookName.length >= 2) {
            updateUIState(uiStateValue.copy(isSearchBookProcess = true))
            searchJob = scope.launch {
                delay(500)
                val response = interactor.searchInBooks(uppercaseBookName)
                val newList = mutableStateListOf<BookShortVo>()

                newList.addAll(response)
                withContext(Dispatchers.Main) {
                    updateUIState(
                        uiStateValue.copy(
                            similarBooks = newList,
                            isSearchBookProcess = false,
                            showSearchBookError = newList.isEmpty()
                        )
                    )
                }
            }
        }
    }

    private fun onSuggestionAuthorClick(author: AuthorVo) {
        updateUIState(uiStateValue.copy(selectedAuthor = author))

        scope.launch {
            delay(DELAY_FOR_LISTENER_PROCESSING)
            clearSearchAuthor()
        }
    }

    private fun urlTextChanged(urlTextFieldValue: TextFieldValue) {
        uiStateValue.bookValues.parsingUrl.value = urlTextFieldValue
        if (urlTextFieldValue.text.isNotEmpty() && urlTextFieldValue.text.length > 5) {
            startParseBook(url = urlTextFieldValue.text)
        } else {
            stopParsingBook()
        }
    }

    private fun startParseBook(url: String) {
        parsingJob?.cancel()
        parsingJob = scope.launch() {
            updateUIState(
                uiStateValue.copy(
                    loadingStatus = LoadingStatus.LOADING,
                    showLoadingIndicator = true
                )
            )

            val response = interactor.parseBookUrl(url)
            if (this@launch.isActive) {
                if (response.bookError != null) {
                    updateUIState(
                        uiStateValue.copy(
                            loadingStatus = LoadingStatus.ERROR
                        )
                    )
                } else if (response.bookItem != null) {
                    updateUIState(
                        uiStateValue.copy(
                            bookItem = response.bookItem
                        )
                    )
                    updateBookInfo()
                    updateUIState(
                        uiStateValue.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    )
                    splitAuthorsNameAndSearch(response.bookItem!!.originalAuthorName)
                }
            }
        }
    }

    private fun updateBookInfo() {
        updateUIState(
            uiStateValue.copy(
                urlFieldIsWork = false
            )
        )
        uiStateValue.bookItem?.let { book ->
            updateBookValues(bookValues = uiStateValue.bookValues, book = book)
        }
    }

    private fun stopParsingBook() {
        parsingJob?.cancel()
        updateUIState(
            uiStateValue.copy(
                showLoadingIndicator = false
            )
        )
    }

    private fun clearUrl() {
        uiStateValue.bookValues.clearAll()
        updateUIState(
            uiStateValue.copy(
                urlFieldIsWork = true,
                showClearButtonOfUrlElement = false,
                showParsingResult = false,
                bookItem = null,
                showDialogClearAllData = false,
            )
        )
    }

    private fun setSelectedDate(millis: Long, text: String) {
        uiStateValue.apply {
            when (datePickerType) {
                DatePickerType.StartDate -> {
                    bookValues.startDateInMillis.value = millis
                    bookValues.startDateInString.value = text
                }

                DatePickerType.EndDate -> {
                    bookValues.endDateInMillis.value = millis
                    bookValues.endDateInString.value = text
                }
            }
        }
    }

    private fun showDatePicker(type: DatePickerType) {
        updateUIState(
            uiStateValue.copy(
                datePickerType = type,
                showDatePicker = true
            )
        )
    }

    private fun updateBookValues(
        bookValues: BookValues,
        book: BookItemVo
    ) {
        bookValues.apply {
            authorName.value = authorName.value.copy(
                text = book.modifiedAuthorName ?: book.originalAuthorName,
                selection = TextRange(book.originalAuthorName.length)
            )
            bookName.value = bookName.value.copy(
                text = book.bookName,
                selection = TextRange(book.bookName.length)
            )
            numberOfPages.value = numberOfPages.value.copy(
                text = book.numbersOfPages.toString(),
                selection = TextRange(book.numbersOfPages.toString().length)
            )
            description.value = description.value.copy(
                text = book.description,
                selection = TextRange(book.description.length)
            )
            selectedStatus.value = ReadingStatus.PLANNED

            coverUrl.value = coverUrl.value.copy(
                text = book.coverUrlFromParsing,
                selection = TextRange(book.coverUrlFromParsing.length)
            )
            isbn.value = isbn.value.copy(
                text = book.isbn,
                selection = TextRange(book.isbn.length)
            )
        }
    }

    private fun finishParsing() {
        if (uiStateValue.loadingStatus == LoadingStatus.SUCCESS) {
            updateUIState(
                uiStateValue.copy(
                    showClearButtonOfUrlElement = true,
                    showLoadingIndicator = false,
                    showParsingResult = true
                )
            )
        }
    }

    private fun setSelectedBook(shortBook: BookShortVo) {
        uiStateValue.bookValues.setShortBook(shortBook)
        updateUIState(
            uiStateValue.copy(
                shortBookItem = shortBook,
            )
        )
    }
}