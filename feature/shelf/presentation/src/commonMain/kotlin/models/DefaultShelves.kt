package models

import main_models.BookItemVo
import main_models.ReadingStatus
import main_models.ShelfVo

val defaultShelves: List<ShelfVo> = listOf(
    ShelfVo(
        id = ShelfVo.generateId(),
        name = ReadingStatus.PLANNED.nameValue,
        booksList = mutableListOf<BookItemVo>()
    ),
    ShelfVo(
        id = ShelfVo.generateId(),
        name = ReadingStatus.READING.nameValue,
        booksList = mutableListOf<BookItemVo>()
    ),
    ShelfVo(
        id = ShelfVo.generateId(),
        name = ReadingStatus.DONE.nameValue,
        booksList = mutableListOf<BookItemVo>()
    ),
    ShelfVo(
        id = ShelfVo.generateId(),
        name = ReadingStatus.DEFERRED.nameValue,
        booksList = mutableListOf<BookItemVo>()
    )
)