import AppConstants.BASE_URL
import com.russhwolf.settings.Settings

class AppConfig() {
    private val settings = Settings()
    val baseUrl: String = BASE_URL
    val authToken
        get() = settings.getString(key = AUTH_TOKEN_KEY, "")

    fun updateAuthToken(token: String) {
        settings.putString(key = AUTH_TOKEN_KEY, value = token)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token_key"
    }
}