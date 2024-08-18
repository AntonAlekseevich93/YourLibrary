import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"

        fun getDateInStringFromMillis(millis: Long, locale: Locale): String {
            try {
                val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
                val date = Date(millis)
                return dateFormat.format(date)
            } catch (_: Throwable) {

            }
            return ""
        }
    }
}