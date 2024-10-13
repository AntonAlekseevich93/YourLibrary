package navigation.screen_components

import com.arkivanov.decompose.ComponentContext
import main_models.books.BookShortVo

interface SingleBookParsingScreenComponent {
    fun onBack()
    fun onCloseScreen()

    /** needSaveScreenId indicates the need to save the id of the current screen
     *  so that when closing subsequent stack screens, stop at the current screen**/
    fun openBookInfo(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean)
}

class DefaultSingleBookParsingScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onCloseScreenListener: () -> Unit,
    private val showBookInfo: (bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean) -> Unit
) : SingleBookParsingScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun onCloseScreen() {
        onCloseScreenListener()
    }

    override fun openBookInfo(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean) {
        showBookInfo(bookId, shortBook, needSaveScreenId)
    }
}