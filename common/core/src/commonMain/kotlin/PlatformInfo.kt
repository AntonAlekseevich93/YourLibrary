import java.util.Calendar

expect class PlatformInfo() {
    fun getCurrentTime(): Calendar
}