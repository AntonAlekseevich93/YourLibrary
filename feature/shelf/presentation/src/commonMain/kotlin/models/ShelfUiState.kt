package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo
import main_models.ShelfVo
import platform.Platform

class ShelfUiState(
    val platform: Platform,
    val shelfList: MutableState<MutableList<ShelfVo>> = mutableStateOf(mutableListOf<ShelfVo>()),
    val config: BookItemCardConfig = BookItemCardConfig(platform),
    val fullShelfIndex: MutableState<Int> = mutableStateOf(-1),
    val sortBookList: MutableState<List<BookItemVo>> = mutableStateOf(emptyList())
) {
    fun searchInFullShelf(searchedText: String, shelfIndex: Int) {
        fullShelfIndex.value = shelfIndex
        val newList = shelfList.value[shelfIndex].booksList.filter {
            it.bookName.contains(searchedText, ignoreCase = true) ||
                    it.authorName.contains(searchedText, ignoreCase = true)
        }
        sortBookList.value = newList
    }

    fun showFullShelf(shelfIndex: Int) {
        sortBookList.value = shelfList.value[shelfIndex].booksList
        fullShelfIndex.value = shelfIndex
    }

    fun addShelf(shelfVo: ShelfVo) {
        shelfList.value.add(shelfVo)
    }

    fun addBooksToShelf(shelfId: String, books: List<BookItemVo>) {
        shelfList.value = shelfList.value.map { shelfVo ->
            if (shelfVo.id == shelfId) {
                shelfVo.copy(booksList = books.toMutableList())
            } else {
                shelfVo
            }
        }.toMutableList()
    }
}
