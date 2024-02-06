package models

import BaseEvent
import androidx.compose.ui.text.input.TextFieldValue

sealed class BookCreatorEvents : BaseEvent {
    data object GoBack : BookCreatorEvents()
    data object CreateBookEvent : BookCreatorEvents()
    class UrlTextChangedEvent(val urlTextFieldValue: TextFieldValue) : BookCreatorEvents()
    data object ClearUrlEvent : BookCreatorEvents()
}