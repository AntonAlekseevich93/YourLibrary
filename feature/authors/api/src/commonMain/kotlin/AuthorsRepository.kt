import kotlinx.coroutines.flow.Flow
import main_models.AuthorVo

interface AuthorsRepository {
    suspend fun createAuthor(author: AuthorVo)
    suspend fun changeMainAuthor(
        oldMainAuthorId: String,
        newMainAuthorId: String,
        newMainAuthorName: String
    )

    suspend fun getAllRelatedAuthors(mainAuthorId: String): List<AuthorVo>
    fun getAuthorByIdWithoutRelates(id: String): AuthorVo?
    suspend fun getAuthorWithRelatesWithoutBooks(authorId: String): AuthorVo?
    suspend fun getAllMainAuthors(): Flow<List<AuthorVo>>
    suspend fun getAllAuthorsNotSeparatingSimilar(): Flow<List<AuthorVo>>
    suspend fun addAuthorToRelates(
        originalAuthorId: String,
        originalAuthorName: String,
        modifiedAuthorId: String,
    )

    suspend fun removeAuthorFromRelates(originalAuthorId: String)
}