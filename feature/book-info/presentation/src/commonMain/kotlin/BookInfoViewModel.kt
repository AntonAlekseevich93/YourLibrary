import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import book_editor.BookEditorEvents
import date.DatePickerEvents
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
import main_models.DatePickerType
import main_models.ReadingStatus
import models.BookInfoScope
import models.BookInfoUiState
import models.BookScreenEvents
import navigation_drawer.contents.models.DrawerEvents
import toolbar.ToolbarEvents
import tooltip_area.TooltipEvents

class BookInfoViewModel(
    private val platformInfo: PlatformInfo,
    private val interactor: BookInfoInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val applicationScope: ApplicationScope,
    private val drawerScope: DrawerScope,
) : BookInfoScope<BaseEvent> {
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

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
            is DrawerEvents.OpenRightDrawerOrCloseEvent -> drawerScope.openRightDrawerOrClose()
            is BookScreenEvents.BookScreenCloseEvent -> applicationScope.closeBookScreen()
            is BookScreenEvents.SaveBookAfterEditing -> saveBookAfterEditing()
            is BookScreenEvents.SetEditMode -> _uiState.value.isEditMode.value = true
            is BookScreenEvents.ChangeReadingStatusEvent -> changeReadingStatus(
                selectedStatus = event.selectedStatus, oldStatusId = event.oldStatusId
            )

            is BookEditorEvents.OnAuthorTextChanged -> onAuthorTextChanged(
                event.textFieldValue,
                event.textWasChanged
            )

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)
            is DatePickerEvents.OnSelectedDate -> setSelectedDate(event.millis, event.text)
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                _uiState.value.showDatePicker.value = false
            }

            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
        }
    }

    fun getBookItem(id: String) {
        scope.launch {
            interactor.getBookById(bookId = id).collect { book ->
                withContext(Dispatchers.Main) {
                    _uiState.value.setBookItem(book)
                }
                launch {
                    val authorId: String = book.modifiedAuthorId ?: book.originalAuthorId
                    interactor.getAuthorWithRelatesWithoutBooks(authorId)?.let {
                        _uiState.value.setSelectedAuthor(it)
                        setSelectedAuthorName()
                    }
                }
            }
        }
    }

    private fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }

    private fun updateBook(bookItem: BookItemVo, needCreateNewAuthor: Boolean) {
        scope.launch {
            if (needCreateNewAuthor) {
                val author = createNewAuthor(authorName = bookItem.originalAuthorName)
                interactor.createAuthor(author)
                interactor.updateBook(
                    bookItem.copy(
                        originalAuthorId = author.id,
                        modifiedAuthorId = null,
                        modifiedAuthorName = null
                    )
                )
            } else {
                interactor.updateBook(bookItem)
            }
        }
    }

    private fun searchAuthor(authorName: String) {
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

    private fun createNewAuthor(
        authorName: String
    ): AuthorVo {
        _uiState.value.apply {
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

    private fun saveBookAfterEditing() {
        uiState.value.apply {
            if (isEditMode.value && bookItem.value != null) {
                bookValues.value.updateBookWithEmptyAuthorId(
                    bookId = bookItem.value!!.id,
                    timestampOfCreating = bookItem.value!!.timestampOfCreating,
                    timestampOfUpdating = getCurrentTimeInMillis(),
                )?.let {
                    val isModifiedAuthor = it.modifiedAuthorName != null

                    val resultBook = if (isModifiedAuthor) {
                        it.copy(
                            modifiedAuthorName = bookValues.value.getChangedAuthorName(),
                            modifiedAuthorId = bookValues.value.modifierAuthorId
                        )
                    } else {
                        it.copy(
                            originalAuthorName = bookValues.value.getChangedAuthorName()
                                ?: throw Exception("BookValues.getChangedAuthorName is null")
                        )
                    }

                    applicationScope.checkIfNeedUpdateBookItem(bookItem.value!!, resultBook)
                    updateBook(
                        bookItem = resultBook,
                        needCreateNewAuthor = needCreateNewAuthor.value
                    )
                }
            }

            isEditMode.value = !isEditMode.value
        }
    }

    private fun changeReadingStatus(selectedStatus: ReadingStatus, oldStatusId: String) {
        scope.launch {
            _uiState.value.bookItem.value?.let { book ->
                interactor.changeBookStatusId(selectedStatus, book.id)
                applicationScope.changedReadingStatus(oldStatusId, book.id)
            }
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

    private fun onSuggestionAuthorClick(author: AuthorVo) {
        _uiState.value.apply {
            setSelectedAuthor(author)
            bookValues.value.authorName.value = bookValues.value.authorName.value.copy(
                author.name,
                selection = TextRange(author.name.length)
            )
            clearSearchAuthor()
        }
    }

    private fun setSelectedAuthorName() {
        _uiState.value.apply {
            selectedAuthor.value?.let { author ->
                val textPostfix = if (author.relatedAuthors.isNotEmpty()) {
                    "(${author.relatedAuthors.joinToString { it.name }})"
                } else ""
                bookValues.value.setSelectedAuthorName(
                    author.name,
                    relatedAuthorsNames = textPostfix
                )
            }
        }
    }

    private fun getCurrentTimeInMillis(): Long = platformInfo.getCurrentTime().timeInMillis
}