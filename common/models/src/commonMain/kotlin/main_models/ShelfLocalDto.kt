package main_models


/** need update DatabaseUtils if change values **/
data class ShelfLocalDto(
    val id: String,
    val name: String,
    val description: String? = "",
)

fun ShelfVo.toLocalDto(): ShelfLocalDto = ShelfLocalDto(
    id = id,
    name = name,
    description = description
)

fun ShelfLocalDto.toVo(booksList: List<BookItemLocalDto>): ShelfVo = ShelfVo(
    id = id,
    name = name,
    description = description ?: "",
    booksList = booksList.map { it.toVo() }.toMutableList()
)
