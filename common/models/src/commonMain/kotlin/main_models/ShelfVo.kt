package main_models

data class ShelfVo(
    val id: Int,
    val name: String,
    val description: String = "",
    val booksList: MutableList<BookItemVo>,
)