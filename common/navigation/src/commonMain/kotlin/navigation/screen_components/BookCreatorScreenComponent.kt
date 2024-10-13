package navigation.screen_components

import com.arkivanov.decompose.ComponentContext
import main_models.books.BookShortVo

interface BookCreatorScreenComponent {

    /** needSaveScreenId indicates the need to save the id of the current screen
     *  so that when closing subsequent stack screens, stop at the current screen**/
    fun openBookInfo(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean)
    fun openUserBookCreatorScreen()
}

class DefaultBookCreatorScreenComponent(
    componentContext: ComponentContext,
    private val showBookInfo: (bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean) -> Unit,
    private val showUserBookCreatorScreen: () -> Unit
) : BookCreatorScreenComponent, ComponentContext by componentContext {

    override fun openBookInfo(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean) {
        showBookInfo(bookId, shortBook, needSaveScreenId)
    }

    override fun openUserBookCreatorScreen() {
        showUserBookCreatorScreen()
    }
}