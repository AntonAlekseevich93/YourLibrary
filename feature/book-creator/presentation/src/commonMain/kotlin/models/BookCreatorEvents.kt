package models

import BaseEvent
import alert_dialog.CommonAlertDialogConfig
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.genre.Genre

sealed class BookCreatorEvents : BaseEvent {
    data object CreateBookEvent : BookCreatorEvents()
    data object ClearUrlEvent : BookCreatorEvents()
    data object OnFinishParsingUrl : BookCreatorEvents()
    data object OnCreateBookManuallyEvent : BookCreatorEvents()
    data object DisableCreateBookManuallyEvent : BookCreatorEvents()
    data object OnClearUrlAndCreateBookManuallyEvent : BookCreatorEvents()
    data object ClearAllBookInfo : BookCreatorEvents()
    data class OnShowDialogClearAllData(val show: Boolean) : BookCreatorEvents()
    class OnShowCommonAlertDialog(val config: CommonAlertDialogConfig) : BookCreatorEvents()
    class SetSelectedGenre(val genre: Genre) : BookCreatorEvents()
    data object DismissCommonAlertDialog : BookCreatorEvents()
    data object SetBookCoverManually : BookCreatorEvents()
    data object HideFullScreenBookSelector : BookCreatorEvents()
    data class OnBookSelected(val shortBook: BookShortVo) : BookCreatorEvents()
    data class SetSelectedBookByMenuClick(val bookId: String) :
        BookCreatorEvents()

    data object ClearSelectedBook : BookCreatorEvents()
    data class ChangeBookReadingStatus(val newStatus: ReadingStatus) : BookCreatorEvents()
}