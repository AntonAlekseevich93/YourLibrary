package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import main_models.AuthorVo
import main_models.BookValues
import main_models.BookVo
import main_models.DatePickerType
import main_models.books.BookShortVo
import main_models.path.PathInfoVo
import main_models.rating_review.ReviewAndRatingVo

data class BookInfoUiState(
    val bookItem: MutableState<BookVo?> = mutableStateOf(null),
    val shortBookItem: MutableState<BookShortVo?> = mutableStateOf(null),
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo()),
    val similarSearchAuthors: SnapshotStateList<AuthorVo> = mutableStateListOf(),
    val selectedAuthor: MutableState<AuthorVo?> = mutableStateOf<AuthorVo?>(null),
    val bookValues: MutableState<BookValues> = mutableStateOf(BookValues()),
    val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    val needCreateNewAuthor: MutableState<Boolean> = mutableStateOf(false),
    var datePickerType: MutableState<DatePickerType> = mutableStateOf(DatePickerType.StartDate),
    val showDatePicker: MutableState<Boolean> = mutableStateOf(false),
    val otherBooksByAuthor: MutableState<List<BookShortVo>> = mutableStateOf(emptyList()),
    val currentBookUserReviewAndRating: MutableState<ReviewAndRatingVo?> = mutableStateOf(null),
    val reviewsAndRatings: MutableState<List<ReviewAndRatingVo>> = mutableStateOf(emptyList()),
    val reviewsCount: MutableState<Int> = mutableStateOf(0),
    val currentDateInMillis: MutableState<Long> = mutableStateOf(0)
) {

    fun setSelectedAuthor(authorVo: AuthorVo) {
        selectedAuthor.value = authorVo
    }

    fun clearSimilarAuthorList() {
        similarSearchAuthors.clear()
    }

}