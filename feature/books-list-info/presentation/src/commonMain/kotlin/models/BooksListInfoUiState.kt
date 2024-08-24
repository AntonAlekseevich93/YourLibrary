package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.books.BookShortVo

data class BooksListInfoUiState(
    val bookList: MutableState<List<BookShortVo>> = mutableStateOf(listOf())
)