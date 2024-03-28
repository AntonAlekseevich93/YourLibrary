import AppConstants.BASE_URL
import com.russhwolf.settings.Settings
import java.util.UUID

class AppConfig() {
    private val settings = Settings()

    val baseUrl: String = BASE_URL

    val authToken
        get() = settings.getString(key = AUTH_TOKEN_KEY, "")

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

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token_key"
        private const val DEVICE_ID_KEY = "device_id_key"
    }
}