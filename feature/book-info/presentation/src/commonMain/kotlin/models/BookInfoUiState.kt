package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import main_models.AuthorVo
import main_models.BookValues
import main_models.BookVo
import main_models.DatePickerType
import main_models.path.PathInfoVo

class BookInfoUiState(
    val bookItem: MutableState<BookVo?> = mutableStateOf(null),
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo()),
    val similarSearchAuthors: SnapshotStateList<AuthorVo> = mutableStateListOf(),
    val selectedAuthor: MutableState<AuthorVo?> = mutableStateOf<AuthorVo?>(null),
    val bookValues: MutableState<BookValues> = mutableStateOf(BookValues()),
    val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    val needCreateNewAuthor: MutableState<Boolean> = mutableStateOf(false),
    var datePickerType: MutableState<DatePickerType> = mutableStateOf(DatePickerType.StartDate),
    val showDatePicker: MutableState<Boolean> = mutableStateOf(false),
) {

    fun setSelectedAuthor(authorVo: AuthorVo) {
        selectedAuthor.value = authorVo
    }

    fun clearSimilarAuthorList() {
        similarSearchAuthors.clear()
    }

}