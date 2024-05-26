package models

import alert_dialog.CommonAlertDialogConfig
import base.BaseUIState
import main_models.AuthorVo
import main_models.BookValues
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.rest.LoadingStatus

data class BookCreatorUiState(
    val shortBookItem: BookShortVo? = null,
    val showLoadingIndicator: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
    val defaultStatus: ReadingStatus = ReadingStatus.PLANNED,
    val selectedAuthor: AuthorVo? = null,
    val similarSearchAuthors: List<AuthorVo> = listOf(),
    val bookValues: BookValues = BookValues(),
    val needCreateNewAuthor: Boolean = false,
    val showClearButtonOfUrlElement: Boolean = false,
    val urlFieldIsWork: Boolean = true,
    val showParsingResult: Boolean = false,
    val showDialogClearAllData: Boolean = false,
    var datePickerType: DatePickerType = DatePickerType.StartDate,
    val showDatePicker: Boolean = false,
    val isCreateBookManually: Boolean = false,
    var similarBooks: List<BookShortVo> = listOf<BookShortVo>(),
    val similarBooksCache: List<BookShortVo> = listOf(),
    val isSearchBookProcess: Boolean = false,
    val isSearchAuthorProcess: Boolean = false,
    val isBookCoverManually: Boolean = false,
    val showCommonAlertDialog: Boolean = false,
    val alertDialogConfig: CommonAlertDialogConfig? = null,
    val showSearchBookError: Boolean = false,
    val showSearchAuthorError: Boolean = false,
) : BaseUIState