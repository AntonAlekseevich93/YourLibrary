import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
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
}