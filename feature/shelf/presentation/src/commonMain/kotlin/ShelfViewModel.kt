import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import main_models.BookItemVo
import main_models.ShelfVo
import models.ShelfUiState
import platform.Platform

class ShelfViewModel(
    private val platform: Platform,
    private val repository: ShelfRepository
) {

    val bookList = mutableStateListOf<BookItemVo>(
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),


        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),

        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен Кинг",
            coverUrl = "https://libcat.ru/uploads/posts/book/dzhillian-bratstvo-konnora.jpg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Почему звёзды умирают",
            authorName = "Агата Кристи",
            coverUrl = " https://www.bookvoed.ru/files/1836/12/56/97/04.jpeg"
        ),
        BookItemVo(
            id = 1,
            shelfId = 1,
            bookName = "Оно",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg"
        ),
    )


    val first = ShelfVo(
        id = 1,
        name = "Хочу прочитать",
        booksList = bookList
    )

    val shelfList: MutableList<ShelfVo> = mutableStateListOf(first, first, first)

    private val _uiState =
        MutableStateFlow(ShelfUiState(platform = platform, shelfList = shelfList))
    val uiState = _uiState.asStateFlow()

    fun searchInShelf(searchedText: String, shelfIndex: Int) {
        _uiState.value.searchInFullShelf(searchedText, shelfIndex)
    }

    fun showFullShelf(shelfIndex: Int) {
        _uiState.value.showFullShelf(shelfIndex = shelfIndex)
    }

}





