package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo
import main_models.ReadingStatusVo
import main_models.ShelfVo
import platform.Platform

class ShelfUiState(
    val platform: Platform,
    val shelvesList: MutableState<MutableList<ShelfVo>> = mutableStateOf(mutableListOf()),
    val config: BookItemCardConfig = BookItemCardConfig(platform),
    val fullShelfIndex: MutableState<Int> = mutableStateOf(-1),
    val sortBookList: MutableState<List<BookItemVo>> = mutableStateOf(emptyList()),
    val bottomSheetExpandEvent: MutableState<() -> Unit> = mutableStateOf({}),
) {
    init {
        shelvesList.value = ReadingStatusVo.createShelvesListFromStatuses().toMutableList()
    }

    //todo это надо вынести в ViewModel. А не выполнять логику в compose
    fun searchInFullShelf(searchedText: String, shelfIndex: Int) {
        fullShelfIndex.value = shelfIndex
        val newList = shelvesList.value[shelfIndex].booksList.filter {
            it.bookName.contains(searchedText, ignoreCase = true) ||
                    (it.modifiedAuthorName != null && it.modifiedAuthorName!!.contains(
                        searchedText,
                        ignoreCase = true
                    ))
                    ||
                    (it.modifiedAuthorName == null && it.originalAuthorName.contains(
                        searchedText,
                        ignoreCase = true
                    ))
        }
        sortBookList.value = newList
    }

    fun showFullShelf(shelfIndex: Int) {
        sortBookList.value = shelvesList.value[shelfIndex].booksList
        fullShelfIndex.value = shelfIndex
    }

    fun addBooksToShelf(shelfId: String, books: List<BookItemVo>) {
        shelvesList.value = shelvesList.value.map { shelfVo ->
            if (shelfVo.id == shelfId) {
                shelfVo.copy(booksList = books.toMutableList())
            } else {
                shelfVo
            }
        }.toMutableList()
    }
}
