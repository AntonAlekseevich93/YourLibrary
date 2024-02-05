package models

import BaseEvent

sealed class BookCreatorEvents : BaseEvent {
    data object GoBack : BookCreatorEvents()
}