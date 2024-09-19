import AppConstants.BASE_HTTPS_URL
import AppConstants.BASE_HTTP_URL
import AppConstants.SHEME_HTTP
import AppConstants.SHEME_HTTPS
import com.russhwolf.settings.Settings
import java.util.UUID

class AppConfig() {
    private val settings = Settings()
    private val isCustomHost = useCustomHost

    val customUrl = settings.getString(CUSTOM_URL, "")
    val useHttp = settings.getBoolean(USE_HTTP, false)

    val baseUrl: String = if (useHttp) {
        if (isCustomHost) "$SHEME_HTTP$customUrl/" else BASE_HTTP_URL
    } else {
        if (isCustomHost) "$SHEME_HTTPS$customUrl/" else BASE_HTTPS_URL
    }


    val authToken
        get() = settings.getString(key = AUTH_TOKEN_KEY, defaultValue = DEFAULT_LOCAL_TOKEN)

    val isAuth
        get() = authToken != DEFAULT_LOCAL_TOKEN

    val userId
        get() = settings.getLong(key = currentUserEmail, defaultValue = -1)

    val useCustomHost
        get() = settings.getBoolean(key = NEED_USE_CUSTOM_HOST, defaultValue = false)

    val useNonModerationRange
        get() = settings.getBoolean(key = NEED_USE_NON_MODERATION_RANGE, defaultValue = false)

    val startNonModerationRange
        get() = settings.getString(NON_MODERATION_START_RANGE, defaultValue = "")

    val endNonModerationRange
        get() = settings.getString(NON_MODERATION_END_RANGE, defaultValue = "")

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

    fun changeUseCustomHost(isCustom: Boolean) {
        settings.putBoolean(key = NEED_USE_CUSTOM_HOST, value = isCustom)
    }

    fun changeUseHttp() {
        settings.putBoolean(key = USE_HTTP, value = !useHttp)
    }

    fun changeUseNonModerationRange(use: Boolean) {
        settings.putBoolean(key = NEED_USE_NON_MODERATION_RANGE, value = use)
    }

    fun changeCustomUrl(url: String) {
        settings.putString(key = CUSTOM_URL, value = url)
    }

    fun changeNonModerationStartRange(startRange: String) {
        settings.putString(key = NON_MODERATION_START_RANGE, value = startRange)
    }

    fun changeNonModerationEndRange(endRange: String) {
        settings.putString(key = NON_MODERATION_END_RANGE, value = endRange)
    }

    fun getNonModerationRangeOrNull(): IntRange? {
        val start = settings.getString(NON_MODERATION_START_RANGE, "").toIntOrNull() ?: return null
        val end = settings.getString(NON_MODERATION_END_RANGE, "").toIntOrNull() ?: return null
        return IntRange(start, end)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token_key"
        private const val DEVICE_ID_KEY = "device_id_key"
        private const val DEFAULT_LOCAL_TOKEN = "default_local_token"
        private const val CURRENT_USER_EMAIL_KEY = "current_user_email_key"
        private const val DEFAULT_LOCAL_EMAIL = "default_local_email"
        private const val CUSTOM_URL = "CUSTOM_URL"
        private const val USE_HTTP = "USE_HTTP"
        private const val NON_MODERATION_START_RANGE = "NON_MODERATION_START_RANGE"
        private const val NON_MODERATION_END_RANGE = "NON_MODERATION_END_RANGE"
        private const val NEED_USE_CUSTOM_HOST = "NEED_USE_CUSTOM_HOST"
        private const val NEED_USE_NON_MODERATION_RANGE = "NEED_USE_NON_MODERATION_RANGE"
    }
}