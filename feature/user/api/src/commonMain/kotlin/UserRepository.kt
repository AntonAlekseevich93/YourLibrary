import kotlinx.coroutines.flow.Flow
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.rest.users.UserStatusVo
import main_models.user.UserVo

interface UserRepository {
    suspend fun getUserStatus(): UserStatusVo?

    suspend fun signUp(
        request: AuthRegisterRequest
    ): BaseResponse<AuthResponse, String>?

    suspend fun signIn(
        request: AuthRequest
    ): BaseResponse<AuthResponse, String>?

    suspend fun createOrUpdateUser(
        id: Int,
        name: String,
        email: String,
        isVerified: Boolean,
        isAuthorized: Boolean
    )

    suspend fun getAuthorizedUser(): Flow<UserVo?>
    suspend fun logOut()
    suspend fun updateUserInfo()

}