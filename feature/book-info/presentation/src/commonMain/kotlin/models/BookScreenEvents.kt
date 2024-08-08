package models

import BaseEvent
import main_models.ReadingStatus

sealed class BookScreenEvents : BaseEvent {
    data object BookScreenCloseEvent : BookScreenEvents()
    data object SaveBookAfterEditing : BookScreenEvents()
    data object SetEditMode : BookScreenEvents()
    class ChangeReadingStatusEvent(val selectedStatus: ReadingStatus, bookId: String) :
        BookScreenEvents()
}