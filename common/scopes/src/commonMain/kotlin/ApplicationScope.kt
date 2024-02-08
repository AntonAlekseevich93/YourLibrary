import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource
import main_models.BookItemVo

interface ApplicationScope {
    fun closeBookScreen()
    fun openBook(painter: Resource<Painter>?, bookId: String)

    fun checkIfNeedUpdateBookItem(oldItem: BookItemVo, newItem: BookItemVo)

    fun changedReadingStatus(oldStatusId: String, bookId: String)
}