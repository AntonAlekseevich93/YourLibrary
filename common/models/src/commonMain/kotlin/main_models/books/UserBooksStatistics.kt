package main_models.books

data class UserBooksStatistics(
    val allBooksCount: Int = 0,
    val plannedBooksCount: Int = 0,
    val readingBooksCount: Int = 0,
    val doneBooksCount: Int = 0,
    val deferredBooksCount: Int = 0,
    val plannedThisYearBooks: Int = 0,
    val finishedThisYearBooks: Int = 0,
    val currentYear: Int = 0
)