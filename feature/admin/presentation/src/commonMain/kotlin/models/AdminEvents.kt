package models

import BaseEvent
import main_models.books.BookShortVo

sealed class AdminEvents : BaseEvent {
    data object GetBooksForModerating : AdminEvents()
    data object ApprovedBook : AdminEvents()
    data object CanceledBook : AdminEvents()
    data object ApprovedWithChangesBook : AdminEvents()
    data object UploadBookCover : AdminEvents()
    data class SelectBook(val selectedBook: BookShortVo) : AdminEvents()
}