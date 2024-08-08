interface ApplicationScope {
    fun closeBookScreen()
    fun openBook(bookId: Long)
    fun changedReadingStatus(oldStatusId: String, bookId: String)
}