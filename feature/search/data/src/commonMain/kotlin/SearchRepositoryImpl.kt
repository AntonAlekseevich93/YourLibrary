import database.LocalSearchDataSource
import main_models.BookItemVo

class SearchRepositoryImpl(
    private val localSearchDataSource: LocalSearchDataSource
) : SearchRepository {

    val test1 = "Помыть полы"
    val test =
        "Пример поискового запроса если вдруг он был обнаружен или вы хотите сказать мне что он был обнаружен"

    val test2 = "Выбросить мусор"
    val test3 =
        "Здесь мы можем написать любую аннотацию которая была вдруг обнаружена и можно сказать что она действительно стоит того чтобы быть обнаруженной и выйти за пределы текста"


    override fun searchTasks(searchText: String): List<BookItemVo> {
        val list = mutableListOf<BookItemVo>()
        if (test.contains(searchText, ignoreCase = true) || test1.contains(
                searchText,
                ignoreCase = true
            )
        ) {
            list.add(BookItemVo(0, 1, name = test1, description = test))
        }
        if (test2.contains(searchText, ignoreCase = true) || test3.contains(
                searchText,
                ignoreCase = true
            )
        ) {
            list.add(BookItemVo(0, 1, name = test2, description = test3))
        }
        return list
    }
}