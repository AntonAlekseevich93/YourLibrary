package main_models

data class BookVo(
    val roomId: Long?,
    val bookId: String,
    val serverId: Int?,
    val originalAuthorId: String,
    val bookName: String,
    val originalAuthorName: String,
    val coverUrl: String?,
    val userCoverUrl: String?,
    val pageCount: Int,
    val isbn: String?,
    val readingStatus: ReadingStatus = ReadingStatus.PLANNED,
    val ageRestrictions: String?,
    val bookGenreId: Int,
    val bookGenreName: String,
    val startDateInString: String?,
    val endDateInString: String?,
    val startDateInMillis: Long?,
    val endDateInMillis: Long?,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
    val isRussian: Boolean?,
    val imageName: String?,
    val description: String,
)