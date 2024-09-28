package models

import BaseEvent
import alert_dialog.CommonAlertDialogConfig
import main_models.ReadingStatus

sealed class BookCreatorEvents : BaseEvent {
    data object ClearUrlEvent : BookCreatorEvents()
    data object OnFinishParsingUrl : BookCreatorEvents()
    data object ClearAllBookInfo : BookCreatorEvents()
    data class OnShowDialogClearAllData(val show: Boolean) : BookCreatorEvents()
    class OnShowCommonAlertDialog(val config: CommonAlertDialogConfig) : BookCreatorEvents()
    data object DismissCommonAlertDialog : BookCreatorEvents()
    data class SetSelectedBookByMenuClick(val bookId: String) :
        BookCreatorEvents()

    data object ClearSelectedBook : BookCreatorEvents()
    data class ChangeBookReadingStatus(val newStatus: ReadingStatus) : BookCreatorEvents()
    data object ClearAuthorSearch : BookCreatorEvents()
    data object ClearBooksSearch : BookCreatorEvents()

    data object CreateManuallyBook : BookCreatorEvents()
}