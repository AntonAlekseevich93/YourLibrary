package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo

class BookCreatorUiState(
    val bookItem: MutableState<BookItemVo?> = mutableStateOf(null),
) {

}