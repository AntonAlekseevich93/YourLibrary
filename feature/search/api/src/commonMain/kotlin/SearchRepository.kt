import main_models.AuthorVo
import main_models.BookVo
import main_models.books.BookShortVo

interface SearchRepository {
    suspend fun searchInAuthorsName(searchedText: String): List<AuthorVo>
    suspend fun searchInBooks(uppercaseBookName: String): List<BookShortVo>
    suspend fun getAllBooksByAuthor(id: String): List<BookShortVo>
    suspend fun searchInLocalBooks(searchText: String): List<BookVo>
}