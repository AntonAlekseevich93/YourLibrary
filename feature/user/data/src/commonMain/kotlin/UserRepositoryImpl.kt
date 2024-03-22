import database.LocalUserDataSource
import ktor.RemoteUserDataSource

class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource
) : UserRepository {

}