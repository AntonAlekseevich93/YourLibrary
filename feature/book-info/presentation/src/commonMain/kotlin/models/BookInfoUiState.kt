package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.BookValues
import main_models.path.PathInfoVo

class BookInfoUiState(
    val bookItem: MutableState<BookItemVo?> = mutableStateOf(null),
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo()),
    val similarSearchAuthors: SnapshotStateList<AuthorVo> = mutableStateListOf(),
    val selectedAuthor: MutableState<AuthorVo?> = mutableStateOf<AuthorVo?>(null),
    val authorWasSelectedProgrammatically: MutableState<() -> Unit> = mutableStateOf({}),
    val bookValues: MutableState<BookValues> = mutableStateOf(BookValues()),
    val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    val needCreateNewAuthor: MutableState<Boolean> = mutableStateOf(false),
) {
    fun setBookItem(book: BookItemVo) {
        bookItem.value = book
    }

    fun addSimilarAuthors(similarAuthors: List<AuthorVo>) {
        similarSearchAuthors.clear()
        similarSearchAuthors.addAll(similarAuthors)
    }

    fun clearSelectedAuthor() {
        selectedAuthor.value = null
    }

    fun setSelectedAuthor(authorVo: AuthorVo) {
        selectedAuthor.value = authorVo
    }

    fun clearSimilarAuthorList() {
        similarSearchAuthors.clear()
    }

}