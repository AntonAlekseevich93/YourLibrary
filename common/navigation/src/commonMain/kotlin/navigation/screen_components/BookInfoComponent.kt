package navigation.screen_components

import com.arkivanov.decompose.ComponentContext
import main_models.books.BookShortVo
import main_models.rating_review.ReviewAndRatingVo

interface BookInfoComponent {
    val previousScreenIsBookInfo: Boolean
    val shortBook: BookShortVo?
    fun onBackClicked()
    fun onCloseClicked()
    fun openBookInfo(shortVo: BookShortVo?, bookId: Long?)
    fun getBookIdOrNull(): Long?
    fun getSavedScrollPosition(): Int
    fun updateScrollPosition(newPosition: Int)
    fun openAuthorsBooks(
        screenTitle: String,
        authorId: String,
        books: List<BookShortVo>,
        needSaveScreenId: Boolean
    )

    fun openReviews(reviews: List<ReviewAndRatingVo>, scrollToReviewId: Int?)
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
    private var savedScrollPosition: Int = 0
    override fun getBookIdOrNull(): Long? = bookId

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

    override fun getSavedScrollPosition(): Int = savedScrollPosition
    override fun updateScrollPosition(newPosition: Int) {
        savedScrollPosition = newPosition
    }

    override fun openReviews(reviews: List<ReviewAndRatingVo>, scrollToReviewId: Int?) {
        openReviewsListener(reviews, scrollToReviewId)
    }
}
