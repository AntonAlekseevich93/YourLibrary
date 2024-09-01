package platform

import java.util.Calendar

expect class PlatformInfoData {
    val isHazeBlurEnabled: Boolean
    val isCanUseModifierBlur: Boolean
    fun getCurrentTime(): Calendar
}