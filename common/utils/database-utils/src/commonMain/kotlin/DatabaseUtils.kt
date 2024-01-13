import com.yourlibrary.database.BooksTable
import com.yourlibrary.database.ShelvesTable
import main_models.BookItemLocalDto
import main_models.ShelfLocalDto

class DatabaseUtils {
    companion object {
        fun ShelvesTable.map() = ShelfLocalDto(
            id = id,
            name = name,
            description = description
        )

        fun BooksTable.map() = BookItemLocalDto(
            id = id,
            shelfId = shelfId,
            bookName = bookName,
            authorName = authorName,
            description = description,
            coverUrl = coverUrl,
            coverUrlFromParsing = coverUrlFromParsing,
            numbersOfPages = numbersOfPages?.toInt(),
            isbn = isbn,
            quotes = quotes,
            readingStatus = readingStatus
        )
    }
}