import androidx.compose.ui.text.TextRange
import book_editor.BookEditorEvents
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
import main_models.DatePickerType
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

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
            is DrawerEvents.OpenRightDrawerOrCloseEvent -> drawerScope.openRightDrawerOrClose()
            is BookScreenEvents.BookScreenCloseEvent -> {
                applicationScope.closeBookScreen()
            }

            is BookScreenEvents.SaveBookAfterEditing -> {
                //todo
            }

            is BookScreenEvents.SetEditMode -> _uiState.value.isEditMode.value = true
            is BookScreenEvents.ChangeReadingStatusEvent -> {
                scope.launch(Dispatchers.IO) {
                    uiState.value.bookItem.value?.let {
                        interactor.changeUserBookReadingStatus(
                            book = it,
                            newStatus = event.selectedStatus
                        )
                    }
                }
            }

            is BookScreenEvents.CloseBookInfoScreen -> {
                navigationHandler.closeBookInfoScreen()
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
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                _uiState.value.showDatePicker.value = false
            }

            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
        }
    }

    fun getBookByLocalId(localBookId: Long) {
        _uiState.value = BookInfoUiState()
        bookJob?.cancel()
        reviewAndRatingJob?.cancel()
        bookJob = scope.launch(Dispatchers.IO) {
            interactor.getLocalBookByLocalId(localBookId).collect { response ->
                response?.let { book ->
                    _uiState.value.bookItem.value = book
                    getCurrentUserReviewAndRatingByBook(book.bookId)
                    getAllReviewsAndRatingsByBookId(book.bookId)
                    getAllBooksByAuthor(book.originalAuthorId)
                }
            }
        }
    }

    fun setShortBook(shortBook: BookShortVo) {
        _uiState.value = BookInfoUiState()
        shortBookJob?.cancel()
        reviewAndRatingJob?.cancel()
        _uiState.value.shortBookItem.value = shortBook
        shortBookJob = scope.launch(Dispatchers.Main) {
            replaceShortBookByLocalBookIfExist(shortBook.bookId)
            getCurrentUserReviewAndRatingByBook(shortBook.bookId)
            getAllReviewsAndRatingsByBookId(shortBook.bookId)
            getAllBooksByAuthor(shortBook.originalAuthorId)
        }
    }

    private fun replaceShortBookByLocalBookIfExist(bookId: String) {
        bookJob?.cancel()
        reviewAndRatingJob?.cancel()
        bookJob = scope.launch(Dispatchers.IO) {
            interactor.getLocalBookById(bookId).collect { response ->
                response?.let { book ->
                    shortBookJob?.cancel()
                    _uiState.value.shortBookItem.value = null
                    _uiState.value.bookItem.value = book
                    getCurrentUserReviewAndRatingByBook(book.bookId)
                    getAllReviewsAndRatingsByBookId(book.bookId)
                    getAllBooksByAuthor(book.originalAuthorId)
                }
            }
        }
    }

    private suspend fun getCurrentUserReviewAndRatingByBook(bookId: String) {
        scope.launch(Dispatchers.IO) {
            interactor.getCurrentUserReviewAndRatingByBook(bookId).collect { reviewAndRating ->
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

    private suspend fun getAllReviewsAndRatingsByBookId(bookId: String) {
        reviewAndRatingJob = scope.launch(Dispatchers.IO) {
            val response = interactor.getAllRemoteReviewsAndRatingsByBookId(bookId)
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

            interactor.updateOrCreateRating(
                newRating = newRating,
                bookId = bookId,
                bookAuthorId = bookAuthorId,
                bookGenreId = bookGenreId,
                isCreatedManuallyBook = bookIsCreatedManually,
                bookForAllUsers = bookForAllUsers,
            )
        }
    }

    private fun addReview(reviewText: String) {
        scope.launch(Dispatchers.IO) {
            val bookId: String =
                _uiState.value.bookItem.value?.bookId ?: _uiState.value.shortBookItem.value?.bookId
                ?: return@launch
            interactor.addReview(
                reviewText = reviewText,
                bookId = bookId
            )
        }
    }
}