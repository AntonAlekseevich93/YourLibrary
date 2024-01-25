import main_models.AuthorVo

interface SearchRepository {
    suspend fun searchInAuthorsName(searchedText: String): List<AuthorVo>

}