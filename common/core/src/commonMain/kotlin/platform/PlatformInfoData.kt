package platform

import java.util.Calendar

expect class PlatformInfoData() {
    fun getCurrentTime(): Calendar
}