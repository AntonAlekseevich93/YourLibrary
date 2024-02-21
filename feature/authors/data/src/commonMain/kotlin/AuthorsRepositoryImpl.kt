import database.LocalAuthorsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ktor.RemoteAuthorsDataSource
import main_models.AuthorVo
import main_models.local_models.toDto
import main_models.local_models.toVo

class AuthorsRepositoryImpl(
    private val remoteAuthorsDataSource: RemoteAuthorsDataSource,
    private val localAuthorsDataSource: LocalAuthorsDataSource
) : AuthorsRepository {

    override suspend fun createAuthor(author: AuthorVo) {
        localAuthorsDataSource.createAuthor(author.toDto())
    }

    override suspend fun changeMainAuthor(oldMainAuthorId: String, newMainAuthorId: String) {
        localAuthorsDataSource.changeMainAuthor(oldMainAuthorId, newMainAuthorId)
    }

    override suspend fun getAllRelatedAuthors(mainAuthorId: String): List<AuthorVo> =
        localAuthorsDataSource.getAllRelatedAuthors(mainAuthorId)
            .mapNotNull { it.toVo(emptyList(), emptyList()) }

    override fun getAuthorByIdWithoutRelates(id: String): AuthorVo? =
        localAuthorsDataSource.getAuthorById(id = id)?.toVo(emptyList(), emptyList())

    override suspend fun getAuthorWithRelatesWithoutBooks(authorId: String): AuthorVo? {
        localAuthorsDataSource.getAuthorById(authorId)?.let { author ->
            val relates = localAuthorsDataSource.getAllRelatedAuthors(authorId)
            return author.toVo(relatedAuthors = relates, books = emptyList())
        }
        return null
    }

    override suspend fun getAllMainAuthors(): Flow<List<AuthorVo>> = flow {
        localAuthorsDataSource.getAllMainAuthors().collect { mainAuthors ->
            val authorsVo = mainAuthors.mapNotNull { mainAuthor ->
                val relatedAuthors =
                    mainAuthor.id?.let { localAuthorsDataSource.getAllRelatedAuthors(it) }
                mainAuthor.toVo(
                    relatedAuthors = relatedAuthors ?: emptyList(),
                    books = emptyList()
                )
            }
            emit(authorsVo)
        }
    }

    override suspend fun getAllAuthorsNotSeparatingSimilar(): Flow<List<AuthorVo>> = flow {
        localAuthorsDataSource.getAllAuthors().collect {
            emit(
                it.mapNotNull { it.toVo(emptyList(), emptyList()) }
            )
        }
    }

    override suspend fun addAuthorToRelates(
        originalAuthorId: String,
        originalAuthorName: String,
        modifiedAuthorId: String,
    ) {
        localAuthorsDataSource.addAuthorToRelates(
            originalAuthorId = originalAuthorId,
            originalAuthorName = originalAuthorName,
            modifiedAuthorId = modifiedAuthorId,
        )
    }

    override suspend fun removeAuthorFromRelates(originalAuthorId: String) {
        localAuthorsDataSource.removeAuthorFromRelates(
            originalAuthorId = originalAuthorId
        )
    }

}