package models

import androidx.compose.ui.text.AnnotatedString

class SearchTaskItemUiState(
    val id: Int = -1,
    val name: AnnotatedString = AnnotatedString(""),
    val description: AnnotatedString = AnnotatedString(""),
)