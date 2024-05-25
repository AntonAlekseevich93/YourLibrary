import AppConstants.BASE_URL
import com.russhwolf.settings.Settings
import java.util.UUID

class AppConfig() {
    private val settings = Settings()

    val baseUrl: String = BASE_URL

    val authToken
        get() = settings.getString(key = AUTH_TOKEN_KEY, defaultValue = DEFAULT_LOCAL_TOKEN)

    val userId
        get() = settings.getLong(key = currentUserEmail, defaultValue = -1)

    private val currentUserEmail
        get() = settings.getString(key = CURRENT_USER_EMAIL_KEY, defaultValue = DEFAULT_LOCAL_EMAIL)

    val deviceId: String
        get() {
            val id = settings.getStringOrNull(DEVICE_ID_KEY)
            return if (id == null) {
                val newId = UUID.randomUUID().toString()
                settings.putString(key = DEVICE_ID_KEY, newId)
                newId
            } else {
                id
            }
        }

    fun updateAuthToken(token: String) {
        settings.putString(key = AUTH_TOKEN_KEY, value = token)
    }

    fun setCurrentUserEmail(email: String) {
        settings.putString(key = CURRENT_USER_EMAIL_KEY, value = email)
    }

    fun saveUserId(userId: Long) {
        settings.putLong(key = currentUserEmail, value = userId)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token_key"
        private const val DEVICE_ID_KEY = "device_id_key"
        private const val DEFAULT_LOCAL_TOKEN = "default_local_token"
        private const val CURRENT_USER_EMAIL_KEY = "current_user_email_key"
        private const val DEFAULT_LOCAL_EMAIL = "default_local_email"
    }
}