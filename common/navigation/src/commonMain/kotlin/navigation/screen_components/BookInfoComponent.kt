package navigation.screen_components

import BookInfoViewModel
import com.arkivanov.decompose.ComponentContext
import di.Inject
import main_models.books.BookShortVo
import main_models.rating_review.ReviewAndRatingVo

interface BookInfoComponent {
    val previousScreenIsBookInfo: Boolean
    val shortBook: BookShortVo?
    fun onBackClicked()
    fun onCloseClicked()
    fun openBookInfo(shortVo: BookShortVo?, bookId: Long?)
    fun openAuthorsBooks(
        screenTitle: String,
        authorId: String,
        books: List<BookShortVo>,
        needSaveScreenId: Boolean
    )

    fun openReviews(reviews: List<ReviewAndRatingVo>, scrollToReviewId: Int?)
    fun getBookInfoViewModel(): BookInfoViewModel
    fun initializeViewModel()
}

class DefaultBookInfoComponent(
    componentContext: ComponentContext,
    override val previousScreenIsBookInfo: Boolean,
    override val shortBook: BookShortVo?,
    private val bookId: Long?,
    private val onBack: () -> Unit,
    private val showBookInfo: (bookId: Long?, shortVo: BookShortVo?) -> Unit,
    private val openAuthorsBooksScreen: (screenTitle: String, authorId: String, books: List<BookShortVo>, needSaveScreenId: Boolean) -> Unit,
    private val openReviewsListener: (reviews: List<ReviewAndRatingVo>, scrollToReviewId: Int?) -> Unit,
    private val onCloseScreen: () -> Unit,
) : BookInfoComponent, ComponentContext by componentContext {

    val viewModel = Inject.instance<BookInfoViewModel>()
    private var firstLaunch = true

    override fun initializeViewModel() {
        if (firstLaunch) {
            firstLaunch = false
            bookId?.let {
                viewModel.getBookByLocalId(it)
            }
            shortBook?.let {
                viewModel.setShortBook(it)
            }
        }
    }

    override fun getBookInfoViewModel() = viewModel

    override fun onBackClicked() {
        onBack()
    }

    override fun onCloseClicked() {
        onCloseScreen()
    }

    override fun openBookInfo(shortVo: BookShortVo?, bookId: Long?) {
        showBookInfo(bookId, shortVo)
    }

    override fun openAuthorsBooks(
        screenTitle: String,
        authorId: String,
        books: List<BookShortVo>,
        needSaveScreenId: Boolean,
    ) {
        openAuthorsBooksScreen(screenTitle, authorId, books, needSaveScreenId)
    }

    override fun openReviews(reviews: List<ReviewAndRatingVo>, scrollToReviewId: Int?) {
        openReviewsListener(reviews, scrollToReviewId)
    }
}
