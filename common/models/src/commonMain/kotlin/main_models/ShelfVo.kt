package main_models

import java.util.UUID

data class ShelfVo(
    val id: String,
    val name: String,
    val description: String = "",
    val booksList: List<BookVo>,
) {
    companion object {
        fun generateId(): String = UUID.randomUUID().toString() //todo подумать над другой реализацией создания id
    }
}