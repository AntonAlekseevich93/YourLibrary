package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import base.BaseUIState
import main_models.books.BookShortVo

data class AdminUiState(
    val isLoading: Boolean = false,
    val moderationBookState: ModerationBookState = ModerationBookState(),
    val databaseMenuScreen: MutableState<Boolean> = mutableStateOf(false),
    val skipLongImageLoading: Boolean = false,
    val useCustomHost: Boolean = false,
    val useHttp: Boolean = false,
    val useNonModerationRange: Boolean = false,
    val customUrl: TextFieldValue = TextFieldValue(""),
    val rangeStart: TextFieldValue = TextFieldValue(""),
    val rangeEnd: TextFieldValue = TextFieldValue(""),
    val isHazeBlurEnabled: MutableState<Boolean> = mutableStateOf(true),
) : BaseUIState

data class ModerationBookState(
    val booksForModeration: SnapshotStateList<BookShortVo> = SnapshotStateList(),
    val selectedItem: BookShortVo? = null,
    val isUploadingBookImage: Boolean = false,
    val canSetBookAsApprovedWithoutUploadImage: Boolean = false,
    val moderationChangedName: MutableState<String?> = mutableStateOf(null),
    val showChangedBookNameField: MutableState<Boolean> = mutableStateOf(false),
)