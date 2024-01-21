import com.yourlibrary.database.BooksTable
import com.yourlibrary.database.FilesInfo
import com.yourlibrary.database.ShelvesTable
import main_models.BookItemLocalDto
import main_models.ShelfLocalDto
import main_models.path.PathInfoDto

class DatabaseUtils {
    companion object {
        fun ShelvesTable.map() = ShelfLocalDto(
            id = id,
            name = name,
            description = description
        )

        fun BooksTable.map() = BookItemLocalDto(
            id = id,
            statusId = statusId,
            shelfId = shelfId,
            bookName = bookName,
            authorName = authorName,
            description = description,
            coverUrl = coverUrl,
            coverUrlFromParsing = coverUrlFromParsing,
            numbersOfPages = numbersOfPages?.toInt(),
            isbn = isbn,
            quotes = quotes,
            readingStatus = readingStatus,
            startDateInString = startDateInString,
            endDateInString = endDateInString,
            startDateInMillis = startDateInMillis,
            endDateInMillis = endDateInMillis,
            timestampOfCreating = timestampOfCreating,
            timestampOfUpdating = timestampOfUpdating,
        )

        fun FilesInfo.toPathInfoDto() = PathInfoDto(
            id = id.toInt(),
            path = dbPath,
            libraryName = libraryName,
            dbName = dbName,
            isSelected = isSelected.toInt()
        )
    }
}