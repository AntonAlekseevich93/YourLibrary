package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import main_models.BookItemVo
import main_models.path.PathInfoVo

class BookInfoUiState(
    val bookItem: MutableState<BookItemVo?> = mutableStateOf(null),
    val selectedPathInfo: MutableState<PathInfoVo> = mutableStateOf(PathInfoVo())
) {

}