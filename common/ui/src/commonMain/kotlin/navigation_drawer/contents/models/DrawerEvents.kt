package navigation_drawer.contents.models

import BaseEvent
import io.kamel.core.Resource
import androidx.compose.ui.graphics.painter.Painter


sealed class DrawerEvents : BaseEvent {
    data object OpenLeftDrawerOrCloseEvent : DrawerEvents()
    data object OpenRightDrawerOrCloseEvent : DrawerEvents()
    class OpenBook(val painterSelectedBookInCache: Resource<Painter>?, val bookId: String) :
        DrawerEvents()
}