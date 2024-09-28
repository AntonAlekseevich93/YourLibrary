import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main_models.AuthorVo
import main_models.BookVo
import main_models.ReadingStatus
import main_models.books.BookShortVo
import models.BooksListInfoScope
import models.BooksListInfoScreenEvents
import models.BooksListInfoUiState
import platform.PlatformInfoData

class BooksListInfoViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BooksListInfoInteractor,
    private val bookCreatorInteractor: BookCreatorInteractor,
    private val applicationScope: ApplicationScope,
    private val appConfig: AppConfig,
) : BooksListInfoScope<BaseEvent> {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private var bookJob: Job? = null
    private var shortBookJob: Job? = null
    private var reviewAndRatingJob: Job? = null
    private val _uiState: MutableStateFlow<BooksListInfoUiState> =
        MutableStateFlow(BooksListInfoUiState())
    val uiState = _uiState.asStateFlow()

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is BooksListInfoScreenEvents.OnSetBookForChangeReadingStatus -> {
                setBookForChangeReadingStatus(event.bookId)
            }

            is BooksListInfoScreenEvents.ClearSelectedBookForChangeReadingStatus -> {
                uiState.value.selectedBookForChangeReadingStatus.value = null
            }

            is BooksListInfoScreenEvents.ChangeBookReadingStatus -> {
                changeBookReadingStatus(event.readingStatus)
            }
        }
    }

    private fun changeBookReadingStatus(newStatus: ReadingStatus) {
        scope.launch(Dispatchers.IO) {
            uiState.value.selectedBookForChangeReadingStatus.value?.let { selectedBookInfo ->
                uiState.value.selectedBookForChangeReadingStatus.value = null
                val shortBookIndex =
                    uiState.value.bookList.indexOf(selectedBookInfo).takeIf { it >= 0 }
                val shortBookWithNewStatus = selectedBookInfo.copy(
                    localReadingStatus = newStatus
                )
                val updatedBookList = uiState.value.bookList.map {
                    if (it.bookId == shortBookWithNewStatus.bookId) {
                        shortBookWithNewStatus
                    } else it
                }

                uiState.value.bookList.clear()
                uiState.value.bookList.addAll(updatedBookList)
                if (shortBookIndex != null) {
                    val localBook = interactor.getLocalBookById(shortBookWithNewStatus.bookId)
                    if (localBook == null) {
                        val bookVo =
                            shortBookWithNewStatus.createUserBookBasedOnShortBook(
                                newStatus,
                                userId = appConfig.userId
                            )
                        val authorVo = getOrCreateAuthor(bookVo, shortBook = shortBookWithNewStatus)
                        bookCreatorInteractor.createBook(bookVo, author = authorVo)
                    } else if (localBook.readingStatus.id != newStatus.id) {
                        bookCreatorInteractor.changeUserBookReadingStatus(
                            book = localBook,
                            newStatus = newStatus
                        )
                    }
                }
            }
        }
    }

    fun setBookList(bookList: List<BookShortVo>) {
        _uiState.value.bookList.clear()
        _uiState.value.bookList.addAll(bookList)
        scope.launch(Dispatchers.IO) {
            _uiState.value.bookList.forEachIndexed { index, item ->
                getCurrentUserReviewAndRatingByBook(
                    index,
                    bookId = item.bookId,
                    mainBookId = item.getMainBookIdByShortBook()
                )
                getReadingStatusByBookId(index, item.bookId)
            }
        }
    }

    private suspend fun getCurrentUserReviewAndRatingByBook(
        bookListIndex: Int,
        bookId: String,
        mainBookId: String
    ) {
        interactor.getCurrentUserReviewAndRatingByBook(mainBookId = mainBookId)
            ?.let { reviewAndRating ->
                val bookItem = _uiState.value.bookList.get(bookListIndex)
                if (bookItem.bookId == bookId) {
                    _uiState.value.bookList[bookListIndex] =
                        bookItem.copy(localCurrentUserRating = reviewAndRating)
                }
            }
    }

    private suspend fun getReadingStatusByBookId(bookListIndex: Int, bookId: String) {
        interactor.getBookReadingStatus(bookId)?.let { status ->
            val bookItem = _uiState.value.bookList.get(bookListIndex)
            if (bookItem.bookId == bookId) {
                _uiState.value.bookList[bookListIndex] =
                    bookItem.copy(localReadingStatus = status)
            }
        }
    }

    private fun setBookForChangeReadingStatus(bookId: String) {
        uiState.value.selectedBookForChangeReadingStatus.value =
            uiState.value.bookList.find { it.bookId == bookId }
    }

    private suspend fun getOrCreateAuthor(book: BookVo, shortBook: BookShortVo): AuthorVo {
        val localAuthor = bookCreatorInteractor.getLocalAuthorById(book.originalAuthorId)
        return localAuthor ?: AuthorVo(
            localId = null,
            serverId = null,
            id = shortBook.originalAuthorId,
            name = shortBook.originalAuthorName,
            uppercaseName = shortBook.originalAuthorName.uppercase(),
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isCreatedByUser = false,
        )
    }


}