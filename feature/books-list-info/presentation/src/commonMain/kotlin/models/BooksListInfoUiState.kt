package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import main_models.books.BookShortVo

data class BooksListInfoUiState(
    val bookList: SnapshotStateList<BookShortVo> = mutableStateListOf(),
    val selectedBookForChangeReadingStatus: MutableState<BookShortVo?> = mutableStateOf(null)
)