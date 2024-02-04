package models

import BaseEvent

sealed class BookScreenEvents : BaseEvent {
    data object BookScreenCloseEvent : BookScreenEvents()
    data object SaveBookAfterEditing : BookScreenEvents()
    data object SetEditMode : BookScreenEvents()
}