package models

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.BookVo
import main_models.ReadingStatusVo
import main_models.ShelfVo
import platform.Platform

data class ShelfUiState @OptIn(ExperimentalMaterialApi::class) constructor(
    val platform: Platform,
    val shelvesList: List<ShelfVo> = ReadingStatusVo.createShelvesListFromStatuses(),
    val config: BookItemCardConfig = BookItemCardConfig(platform),
    val fullShelfIndex: Int = -1,
    val sortBookList: List<BookVo> = emptyList(),
    val bottomSheetExpandEvent: () -> Unit = {},
    val isRefreshingState : Boolean =  false
) : BaseUIState {

    //todo это надо вынести в ViewModel. А не выполнять логику в compose
    fun searchInFullShelf(searchedText: String, shelfIndex: Int) {
//        fullShelfIndex.value = shelfIndex
//        val newList = shelvesList.value[shelfIndex].booksList.filter {
//            it.bookName.contains(searchedText, ignoreCase = true) ||
//                    (it.modifiedAuthorName != null && it.modifiedAuthorName!!.contains(
//                        searchedText,
//                        ignoreCase = true
//                    ))
//                    ||
//                    (it.modifiedAuthorName == null && it.originalAuthorName.contains(
//                        searchedText,
//                        ignoreCase = true
//                    ))
//        }
//        sortBookList.value = newList
    }

    fun showFullShelf(shelfIndex: Int) {
//        sortBookList.value = shelvesList.value[shelfIndex].booksList
//        fullShelfIndex.value = shelfIndex
    }
}
