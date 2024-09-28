package models

import DateUtils
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import main_models.BookVo
import main_models.ReadingStatus
import main_models.books.AGE_RESTRICTIONS
import main_models.books.LANG
import main_models.genre.Genre
import java.util.Locale
import java.util.UUID

data class UserBookCreatorUiState(
    val bookNameTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val authorNameTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val descriptionTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val imageUrlTextFieldValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val publicationYear: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val pagesTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val selectedGenre: MutableState<Genre?> = mutableStateOf(null),
    val isbnTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val readingStatus: MutableState<ReadingStatus> = mutableStateOf(ReadingStatus.PLANNED),
    val selectedLang: MutableState<LANG> = mutableStateOf(LANG.RUSSIAN),
    val selectedAge: MutableState<AGE_RESTRICTIONS> = mutableStateOf(AGE_RESTRICTIONS.NON_SELECTED),
    val startDate: MutableState<Long> = mutableStateOf(0),
    val endDate: MutableState<Long> = mutableStateOf(0),
) {
    fun createUserBook(originalAuthorId: String?, userId: Int): BookVo? {
        val bookName = bookNameTextState.value.text.trim()
        val bookId = UUID.randomUUID().toString()
        val authorName = authorNameTextState.value.text.trim()
        val pageNumber = pagesTextState.value.text.toIntOrNull() ?: return null
        val genre = selectedGenre.value ?: return null
        val publicationYear = publicationYear.value.text.toIntOrNull()
        return BookVo(
            bookId = bookId,
            serverId = null,
            localId = null,
            originalAuthorId = originalAuthorId ?: UUID.randomUUID().toString(),
            bookName = bookName,
            bookNameUppercase = bookName.uppercase(),
            originalAuthorName = authorName,
            description = descriptionTextState.value.text.trim(),
            userCoverUrl = imageUrlTextFieldValue.value.text.trim(),
            pageCount = pageNumber,
            isbn = isbnTextState.value.text.trim(),
            readingStatus = readingStatus.value,
            ageRestrictions = selectedAge.value.takeIf { it != AGE_RESTRICTIONS.NON_SELECTED }?.value
                ?: "",
            bookGenreId = genre.id,
            startDateInString = startDate.value.takeIf { it > 0 }
                ?.let { DateUtils.getDateInStringFromMillis(startDate.value, Locale.ROOT) },
            endDateInString = endDate.value.takeIf { it > 0 }
                ?.let { DateUtils.getDateInStringFromMillis(endDate.value, Locale.ROOT) },
            startDateInMillis = startDate.value,
            endDateInMillis = startDate.value,
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isRussian = null,
            imageName = null,
            authorIsCreatedManually = originalAuthorId == null,
            isLoadedToServer = false, //todo что за поле?
            bookIsCreatedManually = true,
            imageFolderId = null,
            ratingValue = 0.0,
            ratingCount = 0,
            reviewCount = 0,
            ratingSum = 0,
            bookForAllUsers = false,
            originalMainBookId = "",
            lang = selectedLang.value.value,
            publicationYear = publicationYear?.toString() ?: "",
            userId = userId
        )
    }
}