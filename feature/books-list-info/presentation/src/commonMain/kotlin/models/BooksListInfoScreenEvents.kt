package models

import BaseEvent
import main_models.books.BookShortVo

sealed class BooksListInfoScreenEvents : BaseEvent {
    class OnBookSelected(
        val shortBook: BookShortVo,
        val needSaveScreenId: Boolean
    ) : BooksListInfoScreenEvents()
}