import androidx.compose.ui.text.TextRange
import book_editor.elements.BookEditorEvents
import common_events.ReviewAndRatingEvents
import date.DatePickerEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main_models.AuthorVo
import main_models.BookVo
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import models.BookInfoScope
import models.BookInfoUiState
import models.BookScreenEvents
import navigation_drawer.contents.models.DrawerEvents
import platform.PlatformInfoData
import toolbar.ToolbarEvents
import tooltip_area.TooltipEvents

class BookInfoViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BookInfoInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val applicationScope: ApplicationScope,
    private val drawerScope: DrawerScope,
    private val appConfig: AppConfig,
) : BookInfoScope<BaseEvent> {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private var bookJob: Job? = null
    private var shortBookJob: Job? = null
    private var reviewAndRatingJob: Job? = null
    private val _uiState: MutableStateFlow<BookInfoUiState> = MutableStateFlow(BookInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        uiState.value.currentDateInMillis.value = platformInfo.getCurrentTime().timeInMillis
        uiState.value.isHazeBlurEnabled.value = platformInfo.isHazeBlurEnabled
        uiState.value.isCanUseModifierBlur.value = platformInfo.isCanUseModifierBlur
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
            is DrawerEvents.OpenRightDrawerOrCloseEvent -> drawerScope.openRightDrawerOrClose()
            is BookScreenEvents.BookScreenCloseEvent -> {
                applicationScope.closeBookInfoScreen()
            }

            is BookScreenEvents.SaveBookAfterEditing -> {
                //todo
            }

            is BookScreenEvents.SetEditMode -> _uiState.value.isEditMode.value = true
            is BookScreenEvents.ChangeReadingStatusEvent -> {
                changeBookReadingStatusIfBookExistOrCreateBookWithNewStatus(
                    newStatus = event.selectedStatus
                )
            }

            is BookScreenEvents.CloseBookInfoScreen -> {
                navigationHandler.closeBookInfoScreen()
            }

            is BookScreenEvents.OnBack -> {
                applicationScope.onBackFromBookScreen()
            }

            is BookScreenEvents.OpenShortBook -> {
                applicationScope.openBookInfoScreen(bookId = null, shortBook = event.shortBook)
            }

            is BookScreenEvents.ShowDateSelector -> {
                uiState.value.datePickerType.value = event.datePickerType
                uiState.value.showDatePicker.value = true
            }

            is ReviewAndRatingEvents.ChangeBookRating -> {
                updateRating(event.newRating)
            }

            is ReviewAndRatingEvents.AddReview -> {
                addReview(event.reviewText)
            }

            is BookEditorEvents.OnAuthorTextChanged -> {
                //todo
            }

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)
            is DatePickerEvents.OnSelectedDate -> setSelectedDate(event.millis, event.text)
            is DatePickerEvents.OnDeleteDate -> deleteDate(event.datePickerType)
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                _uiState.value.showDatePicker.value = false
            }

            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
        }
    }

    fun getBookByLocalId(localBookId: Long) {
        bookJob?.cancel()
        reviewAndRatingJob?.cancel()
        bookJob = scope.launch(Dispatchers.IO) {
            interactor.getLocalBookByLocalId(localBookId).collect { response ->
                response?.let { book ->
                    _uiState.value.bookItem.value = book
                    getCurrentUserReviewAndRatingByBook(mainBookId = book.originalMainBookId)
                    getAllReviewsAndRatingsByBookId(mainBookId = book.originalMainBookId)
                    getAllBooksByAuthor(book.originalAuthorId)
                }
            }
        }
    }

    fun setShortBook(shortBook: BookShortVo) {
        shortBookJob?.cancel()
        reviewAndRatingJob?.cancel()
        _uiState.value.shortBookItem.value = shortBook
        shortBookJob = scope.launch(Dispatchers.Main) {
            replaceShortBookByLocalBookIfExist(shortBook.bookId)
            getCurrentUserReviewAndRatingByBook(mainBookId = shortBook.mainBookId)
            getAllReviewsAndRatingsByBookId(mainBookId = shortBook.mainBookId)
            getAllBooksByAuthor(shortBook.originalAuthorId)
        }
    }

    private fun replaceShortBookByLocalBookIfExist(bookId: String) {
        bookJob?.cancel()
        reviewAndRatingJob?.cancel()
        bookJob = scope.launch(Dispatchers.IO) {
            interactor.getLocalBookById(bookId).collect { response ->
                response?.let { book ->
//                    shortBookJob?.cancel()
                    _uiState.value.shortBookItem.value = null
                    _uiState.value.bookItem.value = book
                    getCurrentUserReviewAndRatingByBook(mainBookId = book.originalMainBookId)
                }
            }
        }
    }

    private suspend fun getCurrentUserReviewAndRatingByBook(mainBookId: String) {
        scope.launch(Dispatchers.IO) {
            interactor.getCurrentUserReviewAndRatingByBook(mainBookId).collect { reviewAndRating ->
                reviewAndRating?.let {
                    _uiState.value.currentBookUserReviewAndRating.value = it
                    if (!it.reviewText.isNullOrEmpty()) {
                        val list = _uiState.value.reviewsAndRatings.value.toMutableList()
                        list.removeAll { it.userId == appConfig.userId.toInt() }
                        list.add(0, it)
                        _uiState.value.reviewsAndRatings.value = list
                        _uiState.value.reviewsCount.value = _uiState.value.reviewsCount.value + 1
                    }
                }
            }
        }
    }

    private suspend fun getAllReviewsAndRatingsByBookId(mainBookId: String) {
        reviewAndRatingJob = scope.launch(Dispatchers.IO) {
            val response = interactor.getAllRemoteReviewsAndRatingsByBookId(mainBookId)
            if (response.isNotEmpty()) {
                val resultList = response.toMutableList()
                resultList.removeAll { it.userId == appConfig.userId.toInt() }
                _uiState.value.reviewsAndRatings.value = resultList
                _uiState.value.reviewsCount.value = resultList.count { it.reviewText != null }
            }
        }
    }

    private fun getAllBooksByAuthor(authorId: String) {
        val selectedBookId = uiState.value.bookItem.value?.bookId
            ?: uiState.value.shortBookItem.value?.bookId.orEmpty()
        scope.launch(Dispatchers.IO) {
            val booksByAuthor =
                interactor.getAllBooksByAuthor(authorId).filter { it.bookId != selectedBookId }
            if (booksByAuthor.isNotEmpty()) {
                _uiState.value.otherBooksByAuthor.value = booksByAuthor
            }
        }
    }


    private fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }

    private fun setSelectedDate(millis: Long, text: String) {
        uiState.value.bookItem.value?.let { book ->
            scope.launch {
                _uiState.value.apply {
                    when (datePickerType.value) {
                        DatePickerType.StartDate -> {
                            interactor.updateBookReadingStartDate(
                                book = book,
                                startDateInMillis = millis,
                                startDateInString = text
                            )
                        }

                        DatePickerType.EndDate -> {
                            interactor.updateBookReadingEndDate(
                                book = book,
                                endDateInMillis = millis,
                                endDateInString = text
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteDate(datePickerType: DatePickerType) {
        uiState.value.bookItem.value?.let { book ->
            scope.launch {
                _uiState.value.apply {
                    when (datePickerType) {
                        DatePickerType.StartDate -> {
                            interactor.deleteBookReadingStartAndEndDate(
                                book = book,
                            )
                        }

                        DatePickerType.EndDate -> {
                            interactor.deleteBookReadingEndDate(
                                book = book,
                            )
                        }
                    }
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

    private fun updateRating(newRating: Int) {
        scope.launch(Dispatchers.IO) {
            val bookId: String =
                _uiState.value.bookItem.value?.bookId ?: _uiState.value.shortBookItem.value?.bookId
                ?: return@launch
            val bookAuthorId = _uiState.value.bookItem.value?.originalAuthorId
                ?: _uiState.value.shortBookItem.value?.originalAuthorId
                ?: return@launch
            val bookGenreId = _uiState.value.bookItem.value?.bookGenreId
                ?: _uiState.value.shortBookItem.value?.bookGenreId
                ?: return@launch
            val bookIsCreatedManually = _uiState.value.bookItem.value?.bookIsCreatedManually
                ?: false
            val bookForAllUsers = _uiState.value.bookItem.value?.bookIsCreatedManually
                ?: true
            val mainBookId = _uiState.value.bookItem.value?.originalMainBookId
                ?: _uiState.value.shortBookItem.value?.mainBookId
                ?: return@launch

            interactor.updateOrCreateRating(
                newRating = newRating,
                bookId = bookId,
                bookAuthorId = bookAuthorId,
                bookGenreId = bookGenreId,
                isCreatedManuallyBook = bookIsCreatedManually,
                bookForAllUsers = bookForAllUsers,
                mainBookId = mainBookId
            )
        }
    }

    private fun changeBookReadingStatusIfBookExistOrCreateBookWithNewStatus(
        newStatus: ReadingStatus,
    ) {
        scope.launch(Dispatchers.IO) {
            if (uiState.value.bookItem.value != null) {
                interactor.changeUserBookReadingStatus(
                    book = uiState.value.bookItem.value!!,
                    newStatus = newStatus
                )
            } else if (uiState.value.shortBookItem.value != null) {
                val bookVo =
                    uiState.value.shortBookItem.value!!.createUserBookBasedOnShortBook(newStatus)
                val authorVo = getOrCreateAuthor(bookVo)
                interactor.createBook(bookVo, author = authorVo)
            }
        }
    }

    private suspend fun getOrCreateAuthor(book: BookVo): AuthorVo {
        val localAuthor = interactor.getLocalAuthorById(book.originalAuthorId)
        return localAuthor ?: AuthorVo(
            serverId = null,
            localId = null,
            id = book.originalAuthorId,
            name = book.originalAuthorName,
            uppercaseName = book.originalAuthorName.uppercase(),
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isCreatedByUser = false
        )
    }

    private fun addReview(reviewText: String) {
        scope.launch(Dispatchers.IO) {
            val mainBookId: String =
                _uiState.value.bookItem.value?.originalMainBookId
                    ?: _uiState.value.shortBookItem.value?.mainBookId
                    ?: return@launch
            interactor.addReview(
                reviewText = reviewText,
                mainBookId = mainBookId
            )
        }
    }
}