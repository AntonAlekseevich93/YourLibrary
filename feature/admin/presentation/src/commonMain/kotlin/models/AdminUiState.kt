package models

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import base.BaseUIState
import main_models.books.BookShortVo

data class AdminUiState(
    val isLoading: Boolean = false,
    val moderationBookState: ModerationBookState = ModerationBookState(),
    val skipLongImageLoading: Boolean = false,
    val useCustomHost: Boolean = false,
    val customUrl: TextFieldValue = TextFieldValue("")
) : BaseUIState

data class ModerationBookState(
    val booksForModeration: SnapshotStateList<BookShortVo> = SnapshotStateList(),
    val selectedItem: BookShortVo? = null,
    val isUploadingBookImage: Boolean = false,
)