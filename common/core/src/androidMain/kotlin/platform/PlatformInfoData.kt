package platform

import java.util.Calendar
import java.util.TimeZone

actual class PlatformInfoData(
    private val hazeBlurEnabled: Boolean,
    private val canUseModifierBlur: Boolean,
) {
    actual val isHazeBlurEnabled
        get() = hazeBlurEnabled

    actual val isCanUseModifierBlur
        get() = this.canUseModifierBlur

    actual fun getCurrentTime(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
}