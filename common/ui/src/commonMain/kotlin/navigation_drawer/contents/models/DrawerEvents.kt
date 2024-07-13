package navigation_drawer.contents.models

import BaseEvent

sealed class DrawerEvents : BaseEvent {
    data object OpenLeftDrawerOrCloseEvent : DrawerEvents()
    data object OpenRightDrawerOrCloseEvent : DrawerEvents()
    class OpenBook(val bookId: String) :
        DrawerEvents()

    data object ToMain : DrawerEvents()
}