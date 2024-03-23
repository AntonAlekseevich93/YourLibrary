import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse

interface UserRepository {
    suspend fun isAuthorized(): Boolean

    suspend fun signUp(
        request: AuthRegisterRequest
    ): BaseResponse<AuthResponse, String>?

    suspend fun signIn(
        request: AuthRequest
    ): BaseResponse<AuthResponse, String>?
}