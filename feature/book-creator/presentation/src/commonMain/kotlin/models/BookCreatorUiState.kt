package models

import androidx.compose.runtime.mutableStateListOf
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
    val bookItem: BookItemVo? = null,
    val shortBookItem: BookShortVo? = null,
    val showLoadingIndicator: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
    val defaultStatus: ReadingStatus = ReadingStatus.PLANNED,
    val selectedAuthor: AuthorVo? = null,
    val similarSearchAuthors: SnapshotStateList<AuthorVo> = mutableStateListOf(),
    val bookValues: BookValues = BookValues(),
    val needCreateNewAuthor: Boolean = false,
    val showClearButtonOfUrlElement: Boolean = false,
    val urlFieldIsWork: Boolean = true,
    val showParsingResult: Boolean = false,
    val showDialogClearAllData: Boolean = false,
    var datePickerType: DatePickerType = DatePickerType.StartDate,
    val showDatePicker: Boolean = false,
    val isCreateBookManually: Boolean = false,
    val similarBooks: SnapshotStateList<BookShortVo> = mutableStateListOf(),
    val isSearchBookProcess: Boolean = false,
    val isBookCoverManually: Boolean = false,
) : BaseUIState