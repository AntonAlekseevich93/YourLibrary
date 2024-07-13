interface ApplicationScope {
    fun closeBookScreen()
    fun openBook(bookId: String)
    fun changedReadingStatus(oldStatusId: String, bookId: String)
}