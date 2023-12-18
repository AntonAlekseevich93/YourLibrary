import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import main_models.BookItemVo
import models.ShelfRowItem
import kotlin.random.Random

class ShelfViewModel {
    val first = ShelfRowItem(
        id = 0,
        bookItem =
        mutableStateListOf(
            BookItemVo(1, 0, "Помыть посуду"),
            BookItemVo(5, 0, "ВыросШирос"),
            BookItemVo(4, 0, "Экипировка")
        )
    )
    val sencond = ShelfRowItem(
        id = 1,
        bookItem = mutableStateListOf(BookItemVo(2, 1, "Приготовить завтрак"))
    )

    val third = ShelfRowItem(
        id = 2,
        bookItem =
        mutableStateListOf<BookItemVo>(BookItemVo(3, 2, "Убраться"))

    )
    private val _kanbanList = MutableStateFlow(
        mutableStateListOf<ShelfRowItem>(
            first,
            sencond,
            third,
        )
    )
    var kanbanList = _kanbanList.asStateFlow()

    var isCurrentlyDragging = MutableStateFlow(false)
        private set

    var items = MutableStateFlow(emptyList<PersonUiItem>())
        private set

    var addedPersons = mutableStateListOf<PersonUiItem>()
        private set

    init {
        items.value = mutableListOf(
            PersonUiItem("Michael", "1", Color.Gray),
            PersonUiItem("Larissa", "2", Color.Blue),
            PersonUiItem("Marc", "3", Color.Green),
        )
    }

    fun startDragging() {
        isCurrentlyDragging.value = true
    }

    fun stopDragging() {
        isCurrentlyDragging.value = false
    }

    fun addPerson(personUiItem: PersonUiItem) {
        println("Added Person")
        addedPersons.add(personUiItem)
    }

    fun deleteItemById(item: BookItemVo, newId: Int, newIndex: Int) {
        _kanbanList.value.forEachIndexed { index, kanbanColumnItem ->
            val count = kanbanColumnItem.bookItem.count { it.id == item.id }
            if (count > 0) {
                println("viewModel ${_kanbanList.value[index].bookItem.joinToString { it.name }}")
                _kanbanList.value[index].bookItem.removeAll { it.id == item.id }
                println("AFTER viewModel ${_kanbanList.value[index].bookItem.joinToString { it.name }}")
            }
        }
        _kanbanList.value[newIndex].bookItem.add(item.copy(kanbanColumnId = newId))
    }

    private val _uiState = MutableStateFlow<MutableList<ShelfBoardVo>>(mutableStateListOf())
    val uiState = _uiState.asStateFlow()

    private val sections =
        mutableStateListOf(
            BookItemVo(id = 0, kanbanColumnId = 0, name = "Задача 1"),
            BookItemVo(id = 1, kanbanColumnId = 0, name = "Задача 2"),
            BookItemVo(id = 2, kanbanColumnId = 0, name = "Задача 3"),
            BookItemVo(id = 3, kanbanColumnId = 0, name = "Задача 4"),
            BookItemVo(id = 4, kanbanColumnId = 0, name = "Задача 5"),
        )


    private val sections2 = mutableStateListOf(
        BookItemVo(id = 0, kanbanColumnId = 0, name = "Кто-то сделать 1"),
        BookItemVo(id = 1, kanbanColumnId = 0, name = "Кто-то сделать 2"),
        BookItemVo(id = 2, kanbanColumnId = 0, name = "Кто-то сделать 3"),
        BookItemVo(id = 3, kanbanColumnId = 0, name = "Кто-то сделать 4"),
        BookItemVo(id = 4, kanbanColumnId = 0, name = "Кто-то сделать 5"),
    )

    fun taskSwap(from: Int, to: Int, columnIndex: Int, shiftOnly: Boolean) {
        val newList = _uiState.value.toMutableList()

        if (!shiftOnly) {
            val toItem = _uiState.value[columnIndex].taskList[to]
            val fromItem = _uiState.value[columnIndex].taskList[from]
            newList[columnIndex].taskList[from] = toItem
            newList[columnIndex].taskList[to] = fromItem
        } else {
            val fromItem = _uiState.value[columnIndex].taskList[from]
            newList[columnIndex].taskList.removeAt(from)
            newList[columnIndex].taskList.add(to, fromItem)
        }
        _uiState.value = newList
    }

    fun swapSections2(from: Int, to: Int) {
        val fromItem = _uiState.value[from]
        val toItem = _uiState.value[to]
        val newList = _uiState.value.toMutableList()
        newList[from] = toItem
        newList[to] = fromItem

        _uiState.value = newList
    }

    fun sectionClicked(item: Section) {
        println("Clicked $item")
    }

    fun sectionClicked2(it: ShelfBoardVo) {

    }

    fun addNewItemInList(itemIndex: Int, columnIndex: Int, item: Any) {
        (item as? BookItemVo)?.let {
            _uiState.value[columnIndex].taskList.add(0, it)
            changeItemPosition(0, itemIndex, columnIndex)
        }
    }

    fun removeItemFromColumn(item: Any, columnIndex: Int) {
        try {
            (item as? BookItemVo)?.let {
                _uiState.value[columnIndex].taskList.remove(it)
            }
        } catch (_: Exception) {
        }
    }

    private fun changeItemPosition(fromIndex: Int, toIndex: Int, columnIndex: Int) {
        try {
            val item = _uiState.value[columnIndex].taskList.get(fromIndex)
            _uiState.value[columnIndex].taskList.removeAt(fromIndex)
            _uiState.value[columnIndex].taskList.add(toIndex, item)
        } catch (_: Exception) {
        }
    }

    init {
        _uiState.value = mutableListOf(
            ShelfBoardVo(name = "Section 1", taskList = sections),
            ShelfBoardVo(name = "Section 2", taskList = sections2),
        )
    }

}


data class Section(
    val id: Int = internalId++,
    val name: String = "",
    val description: String = "",
    val color: Long = Random(id).nextLong()
) {
    companion object {
        private var internalId = 0
    }
}

data class ShelfBoardVo( //todo переименовать в нормальное название
    val id: Int = internalId++,
    val name: String = "",
    val description: String = "",
    val color: Long = Random(id).nextLong(),
    val taskList: MutableList<BookItemVo>
) {
    companion object {
        private var internalId = 0
    }
}

data class PersonUiItem(
    val name: String,
    val id: String,
    val backgroundColor: Color
)