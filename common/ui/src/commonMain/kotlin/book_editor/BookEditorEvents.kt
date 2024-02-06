package book_editor

import BaseEvent
import androidx.compose.ui.text.input.TextFieldValue
import main_models.AuthorVo

sealed class BookEditorEvents : BaseEvent {
    class OnAuthorTextChanged(val textFieldValue: TextFieldValue, val textWasChanged: Boolean) :
        BookEditorEvents()

    class OnSuggestionAuthorClickEvent(val author: AuthorVo) : BookEditorEvents()
}