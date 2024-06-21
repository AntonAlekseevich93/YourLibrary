package main_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import main_models.books.BookShortVo
import main_models.genre.Genre

class BookValues(
    var parsingUrl: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var authorName: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var bookName: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var numberOfPages: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var description: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var selectedStatus: MutableState<ReadingStatus> = mutableStateOf(ReadingStatus.PLANNED),
    var startDateInMillis: MutableState<Long> = mutableStateOf(0),
    var startDateInString: MutableState<String> = mutableStateOf(""),
    var endDateInMillis: MutableState<Long> = mutableStateOf(0),
    var endDateInString: MutableState<String> = mutableStateOf(""),
    var coverUrl: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var userCoverUrl: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var isbn: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val genre: MutableState<Genre?> = mutableStateOf(null)
) {
    private var selectedAuthorName: String = ""
    private var originalAuthorName: String = ""
    private var originalAuthorId: String = ""

    fun clearAll() {
        parsingUrl.value = TextFieldValue()
        authorName.value = TextFieldValue()
        bookName.value = TextFieldValue()
        numberOfPages.value = TextFieldValue()
        description.value = TextFieldValue()
        selectedStatus.value = ReadingStatus.PLANNED
        startDateInMillis.value = 0
        startDateInString.value = ""
        endDateInMillis.value = 0
        endDateInString.value = ""
        coverUrl.value = TextFieldValue()
        isbn.value = TextFieldValue()
    }

    private fun getNumberOfPagesAsIntOrNull(): Int? {
        val onlyNumbers = StringBuilder()
        numberOfPages.value.text.forEach {
            if (it.isDigit()) {
                onlyNumbers.append(it)
            }
        }
        if (onlyNumbers.isNotEmpty()) {
            try {
                return onlyNumbers.toString().toIntOrNull()
            } catch (_: Throwable) {

            }
        }
        return null
    }

    fun setShortBook(book: BookShortVo) {
        authorName.value = TextFieldValue(book.originalAuthorName)
        bookName.value = TextFieldValue(book.bookName)
        numberOfPages.value = TextFieldValue(book.numbersOfPages.toString())
        description.value = TextFieldValue(book.description)
        coverUrl.value =
            TextFieldValue(book.imageResultUrl)
        isbn.value = TextFieldValue(book.isbn)
        originalAuthorName = book.originalAuthorName
        originalAuthorId = book.originalAuthorId
    }

    fun setSelectedAuthorName(authorName: String, relatedAuthorsNames: String) {
        selectedAuthorName = authorName
        this.authorName.value =
            TextFieldValue(text = authorName, selection = TextRange(authorName.length))
    }

    fun isSelectedAuthorNameWasChanged(): Boolean {
        return if (selectedAuthorName != authorName.value.text) {
            selectedAuthorName = ""
            true
        } else false
    }

    fun isRequiredFieldsFilled(): Boolean =
        authorName.value.text.length >= 2 && bookName.value.text.isNotEmpty()
                && numberOfPages.value.text.isNotEmpty() && numberOfPages.value.text.toIntOrNull() != null &&
                description.value.text.length > 3 &&
                genre.value != null


    fun clearCoverUrl() {
        coverUrl.value = TextFieldValue()
    }
}