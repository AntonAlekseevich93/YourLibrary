import main_models.books.BookShortVo

interface ApplicationScope {
    fun closeBookInfoScreen()
    fun openBookInfoScreen(bookId: Long?, shortBook: BookShortVo?, needSaveScreenId: Boolean)
    fun changedReadingStatus(oldStatusId: String, bookId: String)
    fun openModerationScreen()
    fun openModerationBooksScreen()
    fun openModerationBooksCoversScreen()
    fun openAdminParsingBooksScreen()
    fun openAdminPanel()
}