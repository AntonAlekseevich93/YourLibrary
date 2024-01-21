import database.LocalAuthorsDataSource
import ktor.RemoteAuthorsDataSource

class AuthorsRepositoryImpl(
    private val remoteAuthorsDataSource: RemoteAuthorsDataSource,
    private val localAuthorsDataSource: LocalAuthorsDataSource
) : AuthorsRepository {


}