import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import main_models.BookItemVo
import main_models.ReadingStatus

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

    fun getBookItemVoOrNull(shelfId: String): BookItemVo? {
        return BookItemVo(
            id = BookItemVo.generateId(),
            shelfId = shelfId,
            bookName = bookName.value.text.takeIf { it.isNotEmpty() } ?: return null,
            authorName = authorName.value.text.takeIf { it.isNotEmpty() } ?: return null,
            description = description.value.text.takeIf { it.isNotEmpty() } ?: return null,
            coverUrl = "",
            coverUrlFromParsing = coverUrl.value.text.takeIf { it.isNotEmpty() } ?: return null,
            numbersOfPages = getNumberOfPagesAsIntOrNull() ?: 0,
            isbn = isbn.value.text.takeIf { it.isNotEmpty() } ?: return null,
            quotes = "",
            readingStatus = selectedStatus.value
        )
    }
}