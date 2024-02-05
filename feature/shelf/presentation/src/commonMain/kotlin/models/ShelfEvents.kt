package models

import BaseEvent

sealed class ShelfEvents : BaseEvent {
    class ExpandShelfEvent(val index: Int) : ShelfEvents()
}