import database.LocalSearchDataSource
import main_models.AuthorVo
import main_models.local_models.toVo

class SearchRepositoryImpl(
    private val localSearchDataSource: LocalSearchDataSource
) : SearchRepository {

    override suspend fun searchInAuthorsName(searchedText: String): List<AuthorVo> =
        localSearchDataSource.getAllMatchesByAuthorName(searchedText)
            .mapNotNull { it.toVo(emptyList(), emptyList()) }

}