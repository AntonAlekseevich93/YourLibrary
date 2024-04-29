package models

import androidx.compose.runtime.snapshots.SnapshotStateList
import base.BaseUIState
import main_models.books.BookShortVo

data class AdminUiState(
    val booksForModeration: SnapshotStateList<BookShortVo> = SnapshotStateList(),
    val selectedItem: BookShortVo? = null,
    val isLoading: Boolean = false
) : BaseUIState