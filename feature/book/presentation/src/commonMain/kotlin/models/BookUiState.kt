package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo

class BookUiState(
    val bookItem: MutableState<BookItemVo?> = mutableStateOf(null),
) {

}