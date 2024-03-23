import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse

class UserInteractor(
    private val repository: UserRepository,
    private val searchRepository: SearchRepository,
    private val appConfig: AppConfig,
) {
    suspend fun isAuthorized(): Boolean {
        return if (appConfig.isAuthorized) {
            val isAuthorized = repository.isAuthorized()
            if (!isAuthorized) {
                appConfig.setUnathorized()
            }
            isAuthorized
        } else false
    }

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
            if (token.isNotEmpty()) {
                appConfig.setAuthorized()
                appConfig.updateAuthToken(token)
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
                appConfig.setAuthorized()
                appConfig.updateAuthToken(token)
            }
        }

        return response
    }

}