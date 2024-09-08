package navigation.screens

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import main_models.books.BookShortVo

interface BookInfoComponent {
    val model: Value<BookShortVo>?
    fun onBackClicked()
    fun onCloseClicked()
    fun openBookInfo(shortVo: BookShortVo?, bookId: Long?)
    fun setBookShort(shortVo: BookShortVo?)
    fun getBookIdOrNull(): Long?
    fun getSavedScrollPosition(): Int
    fun updateScrollPosition(newPosition: Int)
}

class DefaultBookInfoComponent(
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val showBookInfo: (bookId: Long?) -> Unit,
    private val bookId: Long?,
    private val onCloseScreen: () -> Unit,
) : BookInfoComponent, ComponentContext by componentContext {
    private var savedScrollPosition: Int = 0
    override var model: Value<BookShortVo>? = null
    override fun getBookIdOrNull(): Long? = bookId
    override fun setBookShort(shortVo: BookShortVo?) {
        shortVo?.let {
            model = MutableValue(shortVo)
        }
    }

    override fun onBackClicked() {
        onBack()
    }

    override fun onCloseClicked() {
        onCloseScreen()
    }

    override fun openBookInfo(shortVo: BookShortVo?, bookId: Long?) {
        showBookInfo(bookId)
    }

    override fun getSavedScrollPosition(): Int = savedScrollPosition
    override fun updateScrollPosition(newPosition: Int) {
        savedScrollPosition = newPosition
    }
}
