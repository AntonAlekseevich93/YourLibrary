import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main_models.books.BookShortVo
import models.BooksListInfoScope
import models.BooksListInfoScreenEvents
import models.BooksListInfoUiState
import platform.PlatformInfoData

class BooksListInfoViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BooksListInfoInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val applicationScope: ApplicationScope,
    private val drawerScope: DrawerScope,
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
            is BooksListInfoScreenEvents.OnBookSelected -> {
                applicationScope.openBookInfoScreen(bookId = null, shortBook = event.shortBook)
            }
        }
    }

    fun getBookByLocalId(localBookId: Long) {
        bookJob?.cancel()
        reviewAndRatingJob?.cancel()
        bookJob = scope.launch(Dispatchers.IO) {
            interactor.getLocalBookByLocalId(localBookId).collect { response ->
                response?.let { book ->
//                    _uiState.value.bookItem.value = book
//                    getCurrentUserReviewAndRatingByBook(book.bookId)
//
                }
            }
        }
    }


    private suspend fun getCurrentUserReviewAndRatingByBook(bookId: String) {
        scope.launch(Dispatchers.IO) {
            interactor.getCurrentUserReviewAndRatingByBook(bookId).collect { reviewAndRating ->
                reviewAndRating?.let {
//                    _uiState.value.currentBookUserReviewAndRating.value = it
//                    if (!it.reviewText.isNullOrEmpty()) {
//                        val list = _uiState.value.reviewsAndRatings.value.toMutableList()
//                        list.removeAll { it.userId == appConfig.userId.toInt() }
//                        list.add(0, it)
//                        _uiState.value.reviewsAndRatings.value = list
//                        _uiState.value.reviewsCount.value = _uiState.value.reviewsCount.value + 1
//                    }
                }
            }
        }
    }

    fun setBookList(bookList: List<BookShortVo>) {
        _uiState.value.bookList.value = bookList
    }


}