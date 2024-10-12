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
                appConfig.setCurrentUserEmail(email)
                appConfig.saveUserId(userId = response.result!!.id!!)
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
                appConfig.setCurrentUserEmail(email)
                response.result?.let {
                    it.id?.let { id ->
                        appConfig.saveUserId(userId = id)
                    }

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

    suspend fun getUserInfo() {
        repository.getUserInfo()?.let { userInfo ->
            if (!userInfo.tokenExist) {
                repository.logOut()
            }
            appConfig.changeUserModeratorStatus(userInfo.isModerator)
        }
    }

    suspend fun updateUserInfo() {
        repository.updateUserInfo()
    }

}