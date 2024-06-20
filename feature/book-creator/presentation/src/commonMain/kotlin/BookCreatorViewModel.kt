import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import base.BaseMVIViewModel
import book_editor.BookEditorEvents
import date.DatePickerEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.AuthorVo
import main_models.BookVo
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.rest.LoadingStatus
import models.BookCreatorEvents
import models.BookCreatorUiState
import platform.PlatformInfoData
import text_fields.DELAY_FOR_LISTENER_PROCESSING
import java.util.UUID

class BookCreatorViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BookCreatorInteractor,
    private val navigationHandler: NavigationHandler,
) : BaseMVIViewModel<BookCreatorUiState, BaseEvent>(BookCreatorUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is BookEditorEvents.OnAuthorTextChanged -> onAuthorTextChanged(
                event.textFieldValue,
                event.textWasChanged,
                event.needNewSearch
            )

            is BookEditorEvents.OnBookNameChanged -> {
                searchBookName(event.bookName)
            }

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)
            is BookEditorEvents.OnBookSelected -> setSelectedBook(event.shortBook)
            is BookEditorEvents.OnChangeNeedCreateNewAuthor -> {
                updateUIState(uiStateValue.copy(needCreateNewAuthor = event.needCreate))
            }

            is BookEditorEvents.OnShowAlertDialogDeleteBookCover -> {
                updateUIState(
                    uiStateValue.copy(
                        showCommonAlertDialog = true,
                        alertDialogConfig = event.config.copy(
                            acceptEvent = BookCreatorEvents.SetBookCoverManually,
                            dismissEvent = BookCreatorEvents.DismissCommonAlertDialog
                        ),
                    )
                )
            }

            is BookEditorEvents.OnCreateBookManually -> {
                updateUIState(
                    uiStateValue.copy(
                        isCreateBookManually = true,
                        showSearchAuthorError = false,
                        needCreateNewAuthor = event.setCreateNewAuthor
                    )
                )
            }

            is BookEditorEvents.OnSearchAuthorClick -> {
                searchAuthor(event.name)
            }

            is BookEditorEvents.ClearBookSearch -> {
                if (uiStateValue.selectedAuthor == null) {
                    updateSimilarBooks(emptyList())
                    updateSimilarBooksCache(emptyList())
                } else {
                    updateSimilarBooks(emptyList())
                    uiStateValue.similarBooks = uiStateValue.similarBooksCache.toMutableStateList()
                }
                updateUIState(uiStateValue.copy(showSearchBookError = false))
            }

            is BookEditorEvents.BookHaveReadingStatusEvent -> {
                scope.launch {
                    uiStateValue.snackbarHostState.showSnackbar(event.message)
                }
            }

            is BookCreatorEvents.GoBack -> navigationHandler.goBack()
            is BookCreatorEvents.CreateBookEvent -> createBook()
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

            is BookCreatorEvents.ClearAllBookInfo -> {
                clearAllBookInfo()
            }

            is BookCreatorEvents.OnShowDialogClearAllData -> {
                updateUIState(uiStateValue.copy(showDialogClearAllData = event.show))
            }

            is BookCreatorEvents.OnShowCommonAlertDialog -> {
                updateUIState(
                    uiStateValue.copy(
                        showCommonAlertDialog = true,
                        alertDialogConfig = event.config
                    )
                )
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

    private fun clearAllBookInfo() {
        updateUIState(
            BookCreatorUiState()
        )
    }

    private fun createBook() {
        scope.launch(Dispatchers.IO) {
            val newBook = if (uiStateValue.shortBookItem != null) {
                createUserBookBasedOnShortBook(uiStateValue.shortBookItem!!)
            } else {
                createManuallyUserBook()
            }
            interactor.createBook(newBook)
            clearAllBookInfo()
            navigationHandler.goBack()
        }
    }

    private fun clearSearchAuthor(showError: Boolean = false) {
        updateUIState(
            state = uiStateValue.copy(
                isSearchAuthorProcess = false,
                showSearchAuthorError = showError,
                similarSearchAuthors = emptyList()
            )
        )
    }

    private fun onAuthorTextChanged(
        textFieldValue: TextFieldValue,
        textWasChanged: Boolean,
        needNewSearch: Boolean
    ) {
        updateSimilarBooks(emptyList())
        updateSimilarBooksCache(emptyList())
        if (uiStateValue.selectedAuthor != null && uiStateValue.bookValues.isSelectedAuthorNameWasChanged()) {
            updateUIState(uiStateValue.copy(selectedAuthor = null))
        }

        if (uiStateValue.showSearchBookError) {
            updateUIState(uiStateValue.copy(showSearchBookError = false))
        }

        if (textFieldValue.text.isEmpty()) {
            clearSearchAuthor()
        } else if (needNewSearch) {
            searchJob?.cancel()
            searchJob = scope.launch(Dispatchers.IO) {
                delay(1500)
                searchAuthor(textFieldValue.text)
            }
        }
    }

    private fun searchAuthor(authorName: String) {
        searchJob?.cancel()
        updateUIState(
            uiStateValue.copy(
                isSearchAuthorProcess = false,
                similarSearchAuthors = emptyList(),
                showSearchBookError = false
            )
        )
        val uppercaseName = authorName.trim().uppercase()
        if (authorName.length >= 2) {
            updateUIState(uiStateValue.copy(isSearchAuthorProcess = true))
            searchJob = scope.launch {
                delay(500)
                val response = interactor.searchInAuthorsNameWithRelates(uppercaseName)
                if (response.isNotEmpty()) {
                    val list: MutableList<AuthorVo> = mutableListOf()
                    list.addAll(response.sortedBy { it.name })
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
        if (uiStateValue.selectedAuthor != null) {
            findInSimilarBooks(bookName)
        } else {
            val uppercaseBookName = bookName.trim().uppercase()
            updateSimilarBooks(emptyList())
            updateSimilarBooksCache(emptyList())
            if (bookName.length >= 2) {
                updateUIState(uiStateValue.copy(isSearchBookProcess = true))
                searchJob = scope.launch(Dispatchers.IO) {
                    delay(500)
                    val response = interactor.searchInBooks(uppercaseBookName)
                    val newList = mutableStateListOf<BookShortVo>()

                    newList.addAll(response)
                    newList.map {
                        val status: ReadingStatus? = interactor.getBookStatusByBookId(it.bookId)
                        it.apply { readingStatus = status }
                    }

                    withContext(Dispatchers.Main) {
                        updateUIState(
                            uiStateValue.copy(
                                similarBooks = newList,
                                similarBooksCache = newList,
                                isSearchBookProcess = false,
                                showSearchBookError = newList.isEmpty()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun findInSimilarBooks(bookName: String) {
        val result = getAllBooksFromCacheWhereNameIs(bookName)
        updateUIState(uiStateValue.copy(showSearchBookError = result.isEmpty()))
        updateSimilarBooks(result)
    }

    private fun getAllBooksFromCacheWhereNameIs(name: String): List<BookShortVo> {
        val newList = uiStateValue.similarBooksCache.filter {
            it.bookName.contains(
                name,
                ignoreCase = true
            )
        }
        return newList
    }

    private fun onSuggestionAuthorClick(author: AuthorVo) {
        updateUIState(uiStateValue.copy(selectedAuthor = author))
        scope.launch {
            delay(DELAY_FOR_LISTENER_PROCESSING)
            clearSearchAuthor()
            getAllBooksByAuthor(author)
        }
    }

    private fun getAllBooksByAuthor(author: AuthorVo) {
        updateUIState(uiStateValue.copy(isSearchBookProcess = false, showSearchBookError = false))
        searchJob?.cancel()
        searchJob = scope.launch(Dispatchers.IO) {
            updateUIState(uiStateValue.copy(isSearchBookProcess = true))
            val response = interactor.getAllBooksByAuthor(author.id)

            uiStateValue.similarBooks
            updateSimilarBooksCache(response)
            val bookName = uiStateValue.bookValues.bookName.value.text
            if (bookName.isNotEmpty()) {
                uiStateValue.similarBooks =
                    getAllBooksFromCacheWhereNameIs(bookName).toMutableStateList()
            } else {
                uiStateValue.similarBooks = response.toMutableStateList()
            }

            withContext(Dispatchers.Main) {
                updateUIState(
                    uiStateValue.copy(
                        isSearchBookProcess = false,
                        showSearchBookError = response.isEmpty()
                    )
                )
            }
        }
    }

    private fun updateSimilarBooksCache(list: List<BookShortVo>) {
        updateUIState(
            uiStateValue.copy(similarBooksCache = list)
        )
    }

    private fun updateSimilarBooks(list: List<BookShortVo>) {
        updateUIState(
            uiStateValue.copy(similarBooks = list)
        )
    }

    private fun clearUrl() {
        uiStateValue.bookValues.clearAll()
        updateUIState(
            uiStateValue.copy(
                urlFieldIsWork = true,
                showClearButtonOfUrlElement = false,
                showParsingResult = false,
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

    private fun createUserBookBasedOnShortBook(shortBook: BookShortVo): BookVo =
        BookVo(
            bookId = shortBook.bookId,
            serverId = shortBook.id,
            originalAuthorId = shortBook.originalAuthorId,
            bookName = shortBook.bookName,
            originalAuthorName = shortBook.originalAuthorName,
            description = shortBook.description,
            coverUrl = shortBook.imageResultUrl,
            userCoverUrl = uiStateValue.bookValues.coverUrl.value.text,
            pageCount = shortBook.numbersOfPages,
            isbn = shortBook.isbn,
            readingStatus = uiStateValue.bookValues.selectedStatus.value,
            ageRestrictions = shortBook.ageRestrictions,
            bookGenreId = shortBook.bookGenreId,
            startDateInString = uiStateValue.bookValues.startDateInString.value,
            endDateInString = uiStateValue.bookValues.endDateInString.value,
            startDateInMillis = uiStateValue.bookValues.startDateInMillis.value,
            endDateInMillis = uiStateValue.bookValues.endDateInMillis.value,
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isRussian = shortBook.isRussian,
            imageName = shortBook.imageName,
            authorIsCreatedManually = uiStateValue.selectedAuthor?.isCreatedByUser ?: false,
            isLoadedToServer = false,
            bookIsCreatedManually = false,
            imageFolderId = shortBook.imageFolderId,
        )

    private fun createManuallyUserBook(): BookVo {
        val author = uiStateValue.selectedAuthor
        uiStateValue.bookValues.apply {
            return BookVo(
                bookId = UUID.randomUUID().toString(),
                serverId = null,
                originalAuthorId = author?.id ?: UUID.randomUUID().toString(),
                bookName = bookName.value.text,
                originalAuthorName = author?.name ?: authorName.value.text,
                description = description.value.text,
                coverUrl = null,
                userCoverUrl = coverUrl.value.text,
                pageCount = numberOfPages.value.text.toInt(),
                isbn = isbn.value.text,
                readingStatus = selectedStatus.value,
                ageRestrictions = null,
                bookGenreId = -1, //todo fixThis
                startDateInString = startDateInString.value,
                endDateInString = endDateInString.value,
                startDateInMillis = startDateInMillis.value,
                endDateInMillis = endDateInMillis.value,
                timestampOfCreating = 0,
                timestampOfUpdating = 0,
                isRussian = null,
                imageName = null,
                authorIsCreatedManually = author?.isCreatedByUser ?: true,
                isLoadedToServer = false,
                bookIsCreatedManually = true,
                imageFolderId = null,
            )
        }
    }
}