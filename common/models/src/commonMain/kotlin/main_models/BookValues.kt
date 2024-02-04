package main_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

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
    var isbn: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
) {
    private var selectedAuthorName: String = ""
    val relatedAuthorsNames: MutableState<String> = mutableStateOf("")

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

    fun createBookItemWithoutAuthorIdOrNull(
        timestampOfCreating: Long,
        timestampOfUpdating: Long,
    ): BookItemVo? {
        return BookItemVo(
            id = BookItemVo.generateId(),
            authorId = "",
            statusId = selectedStatus.value.id,
            shelfId = null,//todo
            bookName = bookName.value.text.takeIf { it.isNotEmpty() } ?: return null,
            authorName = authorName.value.text.takeIf { it.isNotEmpty() } ?: return null,
            description = description.value.text,
            coverUrl = "",
            coverUrlFromParsing = coverUrl.value.text,
            numbersOfPages = getNumberOfPagesAsIntOrNull() ?: 0,
            isbn = isbn.value.text,
            quotes = "",
            readingStatus = selectedStatus.value,
            startDateInString = startDateInString.value,
            endDateInString = endDateInString.value,
            startDateInMillis = startDateInMillis.value,
            endDateInMillis = endDateInMillis.value,
            timestampOfCreating = timestampOfCreating,
            timestampOfUpdating = timestampOfUpdating,
        )
    }

    fun updateBookWithEmptyAuthorId(
        bookId: String,
        timestampOfCreating: Long,
        timestampOfUpdating: Long,
    ): BookItemVo? {
        return BookItemVo(
            id = bookId,
            authorId = "",
            statusId = selectedStatus.value.id,
            shelfId = null, //todo
            bookName = bookName.value.text.takeIf { it.isNotEmpty() } ?: return null,
            authorName = authorName.value.text.takeIf { it.isNotEmpty() } ?: return null,
            description = description.value.text,
            coverUrl = "",
            coverUrlFromParsing = coverUrl.value.text,
            numbersOfPages = getNumberOfPagesAsIntOrNull() ?: 0,
            isbn = isbn.value.text,
            quotes = "",
            readingStatus = selectedStatus.value,
            startDateInString = startDateInString.value,
            endDateInString = endDateInString.value,
            startDateInMillis = startDateInMillis.value,
            endDateInMillis = endDateInMillis.value,
            timestampOfCreating = timestampOfCreating,
            timestampOfUpdating = timestampOfUpdating,
        )
    }

    fun setBookItem(book: BookItemVo) {
        authorName.value = TextFieldValue(book.authorName)
        bookName.value = TextFieldValue(book.bookName)
        numberOfPages.value = TextFieldValue(book.numbersOfPages.toString())
        description.value = TextFieldValue(book.description)
        selectedStatus.value = book.readingStatus
        startDateInMillis.value = book.startDateInMillis
        startDateInString.value = book.startDateInString
        endDateInMillis.value = book.endDateInMillis
        endDateInString.value = book.endDateInString
        coverUrl.value =
            TextFieldValue(book.coverUrl.takeIf { it.isNotEmpty() } ?: book.coverUrlFromParsing)
        isbn.value = TextFieldValue(book.isbn)
    }

    fun setSelectedAuthorName(authorName: String, relatedAuthorsNames: String) {
        selectedAuthorName = authorName
        this.relatedAuthorsNames.value = relatedAuthorsNames
        this.authorName.value =
            TextFieldValue(text = authorName, selection = TextRange(authorName.length))
    }

    fun isSelectedAuthorNameWasChanged(): Boolean {
        return if (selectedAuthorName != authorName.value.text) {
            selectedAuthorName = ""
            relatedAuthorsNames.value = ""
            true
        } else false
    }

    fun isRequiredFieldsFilled(): Boolean =
        authorName.value.text.length >= 2 && bookName.value.text.isNotEmpty()
}