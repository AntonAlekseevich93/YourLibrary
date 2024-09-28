package models

import alert_dialog.CommonAlertDialogConfig
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import base.BaseUIState
import main_models.AuthorVo
import main_models.BookValues
import main_models.BookVo
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
    val bookWasNotFound: Boolean = false,
    val authorWasNotFound: Boolean = false,
    val showClearButtonOfUrlElement: Boolean = false,
    val showDialogClearAllData: Boolean = false,
    var datePickerType: MutableState<DatePickerType> = mutableStateOf(DatePickerType.StartDate),
    val showDatePicker: Boolean = false,
    var similarBooks: List<BookShortVo> = listOf<BookShortVo>(),
    val similarBooksCache: List<BookShortVo> = listOf(),
    val isSearchBookProcess: Boolean = false,
    val isSearchAuthorProcess: Boolean = false,
    val isBookCoverManually: Boolean = false,
    val showCommonAlertDialog: Boolean = false,
    val alertDialogConfig: CommonAlertDialogConfig? = null,
    val showSearchBookError: Boolean = false,
    val showSearchAuthorError: Boolean = false,
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),
    val selectedBookByMenuClick: MutableState<SelectedBook?> = mutableStateOf(null),
    val isHazeBlurEnabled: MutableState<Boolean> = mutableStateOf(true),
    var userBookCreatorUiState: UserBookCreatorUiState = UserBookCreatorUiState(),
    var showCreatedManuallyBookAnimation: MutableState<Boolean> = mutableStateOf(false),
    var isServiceDevelopment: MutableState<Boolean> = mutableStateOf(false),
) : BaseUIState {
    fun updateUserBookCreatorUiState() {
        val bookAuthorName = bookValues.authorName.value.text
        val bookName = bookValues.bookName.value.text
        val userBookCreatorBookName = userBookCreatorUiState.bookNameTextState.value.text
        val userBookCreatorAuthorName = userBookCreatorUiState.authorNameTextState.value.text
        if (bookAuthorName.isNotEmpty() && bookAuthorName != userBookCreatorAuthorName || bookName.isNotEmpty() && bookName != userBookCreatorBookName) {
            clearUserBookCreatorUiState()
            userBookCreatorUiState.bookNameTextState.value = TextFieldValue(bookName)
            userBookCreatorUiState.authorNameTextState.value = TextFieldValue(bookAuthorName)
        }
    }

    private fun clearUserBookCreatorUiState() {
        userBookCreatorUiState = UserBookCreatorUiState()
    }
}

data class SelectedBook(
    val bookId: String = "",
    val bookVo: BookVo? = null,
)