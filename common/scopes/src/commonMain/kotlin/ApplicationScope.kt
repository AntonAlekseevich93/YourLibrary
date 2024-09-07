import main_models.books.BookShortVo

interface ApplicationScope {
    fun closeBookInfoScreen()
    fun openBookInfoScreen(bookId: Long?, shortBook: BookShortVo?)
    fun onBackWithCheckViewModelStore()
    fun changedReadingStatus(oldStatusId: String, bookId: String)
    fun navigateToBooksListInfo(books: List<BookShortVo>)
}