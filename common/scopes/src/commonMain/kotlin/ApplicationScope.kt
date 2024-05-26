import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource

interface ApplicationScope {
    fun closeBookScreen()
    fun openBook(painter: Resource<Painter>?, bookId: String)
    fun changedReadingStatus(oldStatusId: String, bookId: String)
}