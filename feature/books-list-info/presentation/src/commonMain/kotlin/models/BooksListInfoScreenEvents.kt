package models

import BaseEvent
import main_models.ReadingStatus

sealed class BooksListInfoScreenEvents : BaseEvent {
    class OnSetBookForChangeReadingStatus(val bookId: String) : BooksListInfoScreenEvents()
    class ChangeBookReadingStatus(val readingStatus: ReadingStatus) : BooksListInfoScreenEvents()
    data object ClearSelectedBookForChangeReadingStatus : BooksListInfoScreenEvents()
}