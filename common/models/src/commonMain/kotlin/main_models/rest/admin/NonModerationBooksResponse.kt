package main_models.rest.admin

import main_models.books.BookShortVo
import main_models.rest.BackendErrors

class NonModerationBooksResponse(
    val books: List<BookShortVo>?,
    val error: BackendErrors?,
)