package navigation.screen_components

import BooksListInfoViewModel
import com.arkivanov.decompose.ComponentContext
import di.Inject
import main_models.books.BookShortVo

interface BooksListInfoScreenComponent {
    val authorId: String?
    val books: List<BookShortVo>
    val screenTitle: String
    fun onBack()
    fun onCloseScreen()
    fun openBookInfo(bookId: Long?, shortBook: BookShortVo?)
    fun getBooksListInfoViewModel(): BooksListInfoViewModel
}

class DefaultBooksListInfoScreenComponent(
    componentContext: ComponentContext,
    override val authorId: String?,
    override val screenTitle: String,
    override val books: List<BookShortVo>,
    val onBackListener: () -> Unit,
    val onCloseListener: () -> Unit,
    val openBookInfoScreen: (bookId: Long?, shortBook: BookShortVo?) -> Unit,
) : BooksListInfoScreenComponent, ComponentContext by componentContext {
    val viewModel = Inject.instance<BooksListInfoViewModel>()

    init {
        viewModel.setBookList(books)
    }

    override fun getBooksListInfoViewModel() = viewModel

    override fun onBack() {
        onBackListener()
    }

    override fun onCloseScreen() {
        onCloseListener()
    }

    override fun openBookInfo(bookId: Long?, shortBook: BookShortVo?) {
        openBookInfoScreen(bookId, shortBook)
    }
}