import database.LocalUserDataSource
import ktor.RemoteUserDataSource
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse

class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : UserRepository {

    override suspend fun signUp(
        request: AuthRegisterRequest
    ): BaseResponse<AuthResponse, String>? = remoteUserDataSource.signUp(
        request = request
    )

    override suspend fun signIn(
        request: AuthRequest
    ): BaseResponse<AuthResponse, String>? = remoteUserDataSource.signIn(
        request = request
    )

    override suspend fun isAuthorized(): Boolean = remoteUserDataSource.isAuthorized()

}