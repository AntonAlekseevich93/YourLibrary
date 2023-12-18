import main_models.BookItemVo

interface SearchRepository {
    fun searchTasks(searchText: String): List<BookItemVo>

}