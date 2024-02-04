package book_editor

import BaseEvent
import androidx.compose.ui.text.input.TextFieldValue

sealed class BookEditorEvents : BaseEvent {
    class OnAuthorTextChanged(val textFieldValue: TextFieldValue, val textWasChanged: Boolean) :
        BookEditorEvents()
}