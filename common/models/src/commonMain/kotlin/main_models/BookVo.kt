package main_models

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
) {
    var remoteImageLink: String? = null
}