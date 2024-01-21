import java.util.Calendar

actual class PlatformInfo {
   actual fun getCurrentTime(): Calendar = Calendar.getInstance()
}