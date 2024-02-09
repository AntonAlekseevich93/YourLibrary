package toolbar

import BaseEvent

sealed class ToolbarEvents: BaseEvent {
    data object OnCloseEvent: ToolbarEvents()
    data object ToMain: ToolbarEvents()
}