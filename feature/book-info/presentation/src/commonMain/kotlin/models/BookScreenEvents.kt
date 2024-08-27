package models

import BaseEvent
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo

sealed class BookScreenEvents : BaseEvent {
    data object BookScreenCloseEvent : BookScreenEvents()
    data object SaveBookAfterEditing : BookScreenEvents()
    data object SetEditMode : BookScreenEvents()
    class ChangeReadingStatusEvent(val selectedStatus: ReadingStatus, bookId: String) :
        BookScreenEvents()

    data object CloseBookInfoScreen : BookScreenEvents()
    data object OnBack : BookScreenEvents()
    class OpenShortBook(val shortBook: BookShortVo) : BookScreenEvents()
    class ShowDateSelector(val datePickerType: DatePickerType) : BookScreenEvents()
}