package book_editor.elements

import BaseEvent
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.ui.text.input.TextFieldValue
import main_models.AuthorVo

sealed class BookEditorEvents : BaseEvent {
    class OnAuthorTextChanged(
        val textFieldValue: TextFieldValue,
        val textWasChanged: Boolean,
        val needNewSearch: Boolean = false
    ) : BookEditorEvents()

    class OnSuggestionAuthorClickEvent(val author: AuthorVo) : BookEditorEvents()
    class OnBookNameChanged(val bookName: String) : BookEditorEvents()
    class OnChangeNeedCreateNewAuthor(val needCreate: Boolean) : BookEditorEvents()
    class OnShowAlertDialogDeleteBookCover(val config: CommonAlertDialogConfig) : BookEditorEvents()
    data class OnSearchAuthorClick(val name: String) : BookEditorEvents()
    data object ClearBookSearch : BookEditorEvents()
    data object HideSearchError : BookEditorEvents()
}