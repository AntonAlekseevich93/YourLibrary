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



}