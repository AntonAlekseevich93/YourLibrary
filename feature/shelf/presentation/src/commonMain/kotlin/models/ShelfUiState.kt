package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo
import main_models.ShelfVo
import platform.Platform

class ShelfUiState(
    val platform: Platform,
    val shelfList: MutableList<ShelfVo> = mutableStateListOf(),
    val config: BookItemCardConfig = BookItemCardConfig(platform),
    val fullShelfIndex: MutableState<Int> = mutableStateOf(-1),
    val sortBookList: MutableState<List<BookItemVo>> = mutableStateOf(emptyList())
) {
    fun searchInFullShelf(searchedText: String, shelfIndex: Int) {
        fullShelfIndex.value = shelfIndex
        val newList = shelfList[shelfIndex].booksList.filter {
            it.bookName.contains(searchedText, ignoreCase = true) ||
                    it.authorName.contains(searchedText, ignoreCase = true)
        }
        sortBookList.value = newList
    }

    fun showFullShelf(shelfIndex: Int) {
        sortBookList.value = shelfList[shelfIndex].booksList
        fullShelfIndex.value = shelfIndex
    }
}
