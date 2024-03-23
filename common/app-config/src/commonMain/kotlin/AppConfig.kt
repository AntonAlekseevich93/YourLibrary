import AppConstants.BASE_URL
import com.russhwolf.settings.Settings

class AppConfig() {
    private val settings = Settings()
    val baseUrl: String = BASE_URL
    val authToken
        get() = settings.getString(key = AUTH_TOKEN_KEY, "")

    val isAuthorized: Boolean
        get() = settings.getBoolean(key = IS_AUTHORIZED_KEY, false)

    fun updateAuthToken(token: String) {
        settings.putString(key = AUTH_TOKEN_KEY, value = token)
    }

    fun setAuthorized() {
        settings.putBoolean(key = IS_AUTHORIZED_KEY, true)
    }

    fun setUnathorized() {
        settings.putBoolean(key = IS_AUTHORIZED_KEY, false)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token_key"
        private const val IS_AUTHORIZED_KEY = "is_authorized_key"
    }
}