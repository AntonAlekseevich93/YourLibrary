import database.LocalAuthorsDataSource
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

    override fun getAuthorById(id: String): AuthorVo? =
        localAuthorsDataSource.getAuthorById(id = id)?.toVo(emptyList(), emptyList())

    override suspend fun getAuthorWithRelatesWithoutBooks(authorId: String): AuthorVo? {
        localAuthorsDataSource.getAuthorById(authorId)?.let { author ->
            val relates = localAuthorsDataSource.getAllRelatedAuthors(authorId)
            return author.toVo(relatedAuthors = relates, books = emptyList())
        }
        return null
    }

    override suspend fun getAllAuthors(): List<AuthorVo> {
        val resultList: MutableList<AuthorVo> = mutableListOf()
        localAuthorsDataSource.getAllMainAuthors().forEach { mainAuthor ->
            val relates = mainAuthor.id?.let { localAuthorsDataSource.getAllRelatedAuthors(it) }
            mainAuthor.toVo(relatedAuthors = relates ?: emptyList(), books = emptyList())?.let {
                resultList.add(it)
            }
        }
        return resultList
    }
}