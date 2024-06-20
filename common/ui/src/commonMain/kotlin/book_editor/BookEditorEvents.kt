package book_editor

import BaseEvent
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.ui.text.input.TextFieldValue
import main_models.AuthorVo
import main_models.books.BookShortVo

sealed class BookEditorEvents : BaseEvent {
    class OnAuthorTextChanged(
        val textFieldValue: TextFieldValue,
        val textWasChanged: Boolean,
        val needNewSearch: Boolean = false
    ) : BookEditorEvents()

    class OnSuggestionAuthorClickEvent(val author: AuthorVo) : BookEditorEvents()
    class OnBookNameChanged(val bookName: String) : BookEditorEvents()
    class OnBookSelected(val shortBook: BookShortVo) : BookEditorEvents()
    class OnChangeNeedCreateNewAuthor(val needCreate: Boolean) : BookEditorEvents()
    class OnShowAlertDialogDeleteBookCover(val config: CommonAlertDialogConfig) : BookEditorEvents()
    class OnCreateBookManually(val setCreateNewAuthor: Boolean = false) : BookEditorEvents()
    data class OnSearchAuthorClick(val name: String) : BookEditorEvents()
    data object ClearBookSearch : BookEditorEvents()
    data class BookHaveReadingStatusEvent(val message: String) : BookEditorEvents()
}