import main_models.AuthorVo

interface AuthorsRepository {
    suspend fun updateAuthorsTimestamp(timestamp: Long)
    suspend fun updateLocalAuthor(author: AuthorVo)
}