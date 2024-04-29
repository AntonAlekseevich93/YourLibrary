package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import base.BaseUIState
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.BookValues
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.rest.LoadingStatus

data class BookCreatorUiState(
    val bookItem: MutableState<BookItemVo?> = mutableStateOf(null),
    val showLoadingIndicator: MutableState<Boolean> = mutableStateOf(false),
    val loadingStatus: MutableState<LoadingStatus> = mutableStateOf(LoadingStatus.LOADING),
    val defaultStatus: MutableState<ReadingStatus> = mutableStateOf(ReadingStatus.PLANNED),
    val selectedAuthor: MutableState<AuthorVo?> = mutableStateOf<AuthorVo?>(null),
    val similarSearchAuthors: SnapshotStateList<AuthorVo> = mutableStateListOf(),
    val bookValues: MutableState<BookValues> = mutableStateOf(BookValues()),
    val needCreateNewAuthor: MutableState<Boolean> = mutableStateOf(false),
    val showClearButtonOfUrlElement: MutableState<Boolean> = mutableStateOf(false),
    val urlFieldIsWork: MutableState<Boolean> = mutableStateOf(true),
    val showParsingResult: MutableState<Boolean> = mutableStateOf(false),
    val showDialogClearAllData: MutableState<Boolean> = mutableStateOf(false),
    var datePickerType: MutableState<DatePickerType> = mutableStateOf(DatePickerType.StartDate),
    val showDatePicker: MutableState<Boolean> = mutableStateOf(false),
    val isCreateBookManually: MutableState<Boolean> = mutableStateOf(true),
    val similarBooks: SnapshotStateList<BookShortVo> = mutableStateListOf(),
) : BaseUIState