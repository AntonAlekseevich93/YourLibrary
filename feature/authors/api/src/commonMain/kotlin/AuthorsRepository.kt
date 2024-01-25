import main_models.AuthorVo

interface AuthorsRepository {
    suspend fun createAuthor(author: AuthorVo)
    suspend fun changeMainAuthor(oldMainAuthorId: String, newMainAuthorId: String)
    suspend fun getAllRelatedAuthors(mainAuthorId: String): List<AuthorVo>
    fun getAuthorById(id: String): AuthorVo?
    suspend fun getAuthorWithRelatesWithoutBooks(authorId: String): AuthorVo?
}