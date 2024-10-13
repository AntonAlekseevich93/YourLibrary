package navigation.screen_components

import com.arkivanov.decompose.ComponentContext
import main_models.books.BookShortVo

interface MainScreenComponent {

    /** needSaveScreenId indicates the need to save the id of the current screen
     *  so that when closing subsequent stack screens, stop at the current screen**/
    fun openBookInfo(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean)
}

class DefaultMainScreenComponent(
    componentContext: ComponentContext,
    private val showBookInfo: (bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean) -> Unit,
) : MainScreenComponent, ComponentContext by componentContext {

    override fun openBookInfo(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean) {
        showBookInfo(bookId, shortBook, needSaveScreenId)
    }
}