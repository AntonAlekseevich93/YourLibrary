package main_models.genre

class Genre(
    val id: Int,
    val name: String,
    val relates: Int?,
    val isSelectable: Boolean = true,
)