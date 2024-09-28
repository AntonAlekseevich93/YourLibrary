package main_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class BookValues(
    var authorName: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    var bookName: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
) {
    private var selectedAuthorName: String = ""
    private var originalAuthorName: String = ""
    private var originalAuthorId: String = ""

    fun clearAll() {
        authorName.value = TextFieldValue()
        bookName.value = TextFieldValue()
    }

    fun setSelectedAuthorName(authorName: String) {
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

    fun clearAuthor() {
        authorName.value = TextFieldValue()
    }

    fun clearBook() {
        bookName.value = TextFieldValue()
    }
}