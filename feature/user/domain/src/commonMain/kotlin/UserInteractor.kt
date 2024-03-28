import kotlinx.coroutines.flow.Flow
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.user.UserVo

class UserInteractor(
    private val repository: UserRepository,
    private val searchRepository: SearchRepository,
    private val appConfig: AppConfig,
) {

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): BaseResponse<AuthResponse, String>? {
        val response = repository.signUp(
            request = AuthRegisterRequest(
                userName = name,
                userEmail = email,
                password = password
            )
        )

        response?.result?.token?.let { token ->
            if (token.isNotEmpty() && response.result?.id != null) {
                appConfig.updateAuthToken(token = token)
                repository.createOrUpdateUser(
                    id = response.result!!.id!!,
                    name = name,
                    email = email,
                    isVerified = response.result?.isVerified ?: false,
                    isAuthorized = true
                )
            }
        }

        return response
    }

    suspend fun signIn(
        email: String,
        password: String
    ): BaseResponse<AuthResponse, String>? {
        val response = repository.signIn(
            request = AuthRequest(
                userEmail = email,
                password = password
            )
        )

        response?.result?.token?.let { token ->
            if (token.isNotEmpty()) {
                appConfig.updateAuthToken(token = token)
                response.result?.let {
                    repository.createOrUpdateUser(
                        id = it.id!!,
                        name = it.name!!,
                        email = email,
                        isVerified = response.result?.isVerified ?: false,
                        isAuthorized = true
                    )
                }
            }
        }

        return response
    }

    suspend fun getAuthorizedUser(): Flow<UserVo?> =
        repository.getAuthorizedUser()

    fun logOut() {
        repository.logOut()
    }

    suspend fun checkIfUserTokenExistAndLogOutIfNot() {
        repository.isTokenExist()?.let { isTokenExist ->
            if (!isTokenExist) {
                repository.logOut()
            }
        }
    }

    suspend fun updateUserInfo() {
        repository.updateUserInfo()
    }

}