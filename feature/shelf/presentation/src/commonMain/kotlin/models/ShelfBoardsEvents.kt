package models

import BaseEvent

sealed class ShelfBoardsEvents : BaseEvent {
    class SetBottomSheetExpandListener(val listener: () -> Unit) : ShelfBoardsEvents()
    data object OnDataRefresh : ShelfBoardsEvents()
}