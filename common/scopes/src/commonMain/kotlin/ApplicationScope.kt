import main_models.books.BookShortVo

interface ApplicationScope {
    fun closeBookScreen()
    fun openBook(bookId: Long?, shortBook: BookShortVo?)
    fun changedReadingStatus(oldStatusId: String, bookId: String)
}