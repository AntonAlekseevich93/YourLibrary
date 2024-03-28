import database.LocalUserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteUserDataSource
import ktor.models.toVo
import main_models.local_models.toVo
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.user.UserVo

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

    override suspend fun isTokenExist(): Boolean? = remoteUserDataSource.isTokenExist()

    override fun createOrUpdateUser(
        id: Int,
        name: String,
        email: String,
        isVerified: Boolean,
        isAuthorized: Boolean
    ) {
        localUserDataSource.createOrUpdateUser(
            id = id,
            name = name,
            email = email,
            isVerified = if (isVerified) 0 else 1,
            isAuthorized = if (isAuthorized) 0 else 1
        )
    }

    override suspend fun getAuthorizedUser(): Flow<UserVo?> =
        localUserDataSource.getAuthorizedUser().map {
            it?.toVo()
        }

    override fun logOut() {
        localUserDataSource.logOut()
    }

    override suspend fun updateUserInfo() {
        remoteUserDataSource.getUser()?.toVo()?.let {
            localUserDataSource.createOrUpdateUser(
                id = it.id.toInt(),
                name = it.name,
                email = it.email,
                isVerified = if (it.isVerified) 0 else 1,
                isAuthorized = 0
            )
        }
    }

}