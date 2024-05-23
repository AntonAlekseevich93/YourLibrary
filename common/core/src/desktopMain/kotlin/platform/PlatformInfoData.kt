package platform

import java.util.Calendar

actual class PlatformInfoData {
   actual fun getCurrentTime(): Calendar = Calendar.getInstance()
}