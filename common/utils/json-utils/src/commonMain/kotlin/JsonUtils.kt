import kotlinx.serialization.json.Json
import kotlinx.serialization.*

class JsonUtils {
    companion object {
        inline fun <reified T> T.encodeToString(): String = Json.encodeToString(this)
        inline fun <reified T> String.decodeToObject(): T = Json.decodeFromString(this)
    }
}