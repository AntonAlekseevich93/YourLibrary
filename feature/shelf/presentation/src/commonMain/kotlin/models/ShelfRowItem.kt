package models

import main_models.BookItemVo

data class ShelfRowItem(val id: Int, var bookItem: MutableList<BookItemVo>)
