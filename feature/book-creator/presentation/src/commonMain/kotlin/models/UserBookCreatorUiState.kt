package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import main_models.ReadingStatus
import main_models.books.AGE_RESTRICTIONS
import main_models.books.LANG
import main_models.genre.Genre

data class UserBookCreatorUiState(
    val bookNameTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val authorNameTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val descriptionTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val imageUrlTextFieldValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val pagesTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val selectedGenre: MutableState<Genre?> = mutableStateOf(null),
    val isbnTextState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val readingStatus: MutableState<ReadingStatus> = mutableStateOf(ReadingStatus.PLANNED),
    val selectedLang: MutableState<LANG> = mutableStateOf(LANG.RUSSIAN),
    val selectedAge: MutableState<AGE_RESTRICTIONS> = mutableStateOf(AGE_RESTRICTIONS.NON_SELECTED),
    val startDate: MutableState<Long> = mutableStateOf(0),
    val endDate: MutableState<Long> = mutableStateOf(0),
)