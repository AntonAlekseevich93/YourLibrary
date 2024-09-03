package main_models

import java.util.concurrent.TimeUnit

data class BookVo(
    val bookId: String,
    val serverId: Int?,
    val localId: Long?,
    val originalAuthorId: String,
    val bookName: String,
    val bookNameUppercase: String,
    val originalAuthorName: String,
    val userCoverUrl: String?,
    val pageCount: Int,
    val isbn: String?,
    val readingStatus: ReadingStatus = ReadingStatus.PLANNED,
    val ageRestrictions: String?,
    val bookGenreId: Int,
    val startDateInString: String?,
    val endDateInString: String?,
    val startDateInMillis: Long?,
    val endDateInMillis: Long?,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
    val isRussian: Boolean?,
    val imageName: String?,
    val description: String,
    val authorIsCreatedManually: Boolean,
    val bookIsCreatedManually: Boolean,
    val isLoadedToServer: Boolean,
    val imageFolderId: Int?,
    val ratingValue: Double,
    val ratingCount: Int,
    val reviewCount: Int,
    val ratingSum: Int,
    val bookForAllUsers: Boolean,
    val originalMainBookId: String,
) {
    var remoteImageLink: String? = null

    fun getReadingDays(): Int? {
        return if (startDateInMillis != null && endDateInMillis != null && startDateInMillis > 0 && endDateInMillis > 0) {
            val durationInMillis: Long = (endDateInMillis - startDateInMillis)
            (TimeUnit.MILLISECONDS.toDays(durationInMillis) + 1).toInt()
        } else null
    }

    fun getReadingDaysByCurrentDay(currentDay: Long): Int? {
        return if (startDateInMillis != null && startDateInMillis > 0 && currentDay > 0) {
            val durationInMillis: Long =
                (currentDay - startDateInMillis).takeIf { it > 0 } ?: return null
            (TimeUnit.MILLISECONDS.toDays(durationInMillis) + 1).toInt()
        } else null
    }
}