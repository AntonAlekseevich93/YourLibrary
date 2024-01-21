package main_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class BooksInfoHeader(
    val name: String,
    val id: String,
    val isShelf: Boolean,
    val priorityInList: Int,
    val isCollapse: MutableState<Boolean> = mutableStateOf(false),
)