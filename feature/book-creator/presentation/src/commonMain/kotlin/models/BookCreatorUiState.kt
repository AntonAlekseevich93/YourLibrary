package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo
import main_models.ReadingStatus
import main_models.rest.LoadingStatus

class BookCreatorUiState(
    val bookItem: MutableState<BookItemVo?> = mutableStateOf(null),
    val showLoadingIndicator: MutableState<Boolean> = mutableStateOf(false),
    val loadingStatus: MutableState<LoadingStatus> = mutableStateOf(LoadingStatus.LOADING),
    val similarAuthorList: MutableState<List<String>> = mutableStateOf(emptyList()),
    val statusesList: MutableState<List<ReadingStatus>> = mutableStateOf(ReadingStatus.entries),
    val defaultStatus: MutableState<ReadingStatus> = mutableStateOf(ReadingStatus.PLANNED),
    val needUpdateBookInfo: MutableState<Boolean> = mutableStateOf(false)
) {
    fun startParsing() {
        loadingStatus.value = LoadingStatus.LOADING
        showLoadingIndicator.value = true
    }

    fun hideLoadingIndicator() {
        showLoadingIndicator.value = false
    }

    fun setParsingError() {
        loadingStatus.value = LoadingStatus.ERROR
    }

    fun setParsingSuccess() {
        loadingStatus.value = LoadingStatus.SUCCESS
    }

    fun clearSimilarAuthorList() {
        similarAuthorList.value = emptyList()
    }

    fun addSimilarAuthor(list: List<String>) {
        similarAuthorList.value = list
    }

    fun clearAllBookData() {
        bookItem.value = null
    }
}