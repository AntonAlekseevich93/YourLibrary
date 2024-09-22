import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main_models.books.BookShortVo
import models.BooksListInfoScope
import models.BooksListInfoUiState
import platform.PlatformInfoData

class BooksListInfoViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BooksListInfoInteractor,
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

}