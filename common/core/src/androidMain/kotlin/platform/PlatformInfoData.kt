package platform

import java.util.Calendar
import java.util.TimeZone

actual class PlatformInfoData {
    actual fun getCurrentTime(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
}