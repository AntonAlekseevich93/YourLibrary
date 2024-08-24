import main_models.books.BookShortVo

interface ApplicationScope {
    fun closeBookInfoScreen()
    fun openBookInfoScreen(bookId: Long?, shortBook: BookShortVo?)
    fun onBackFromBookScreen()
    fun changedReadingStatus(oldStatusId: String, bookId: String)
}