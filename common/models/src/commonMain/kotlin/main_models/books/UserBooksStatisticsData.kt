package main_models.books

import main_models.service_development.BookWithServiceDevelopment

data class UserBooksStatisticsData(
    val allBooksCount: Int = 0,
    val plannedBooksCount: Int = 0,
    val readingBooksCount: Int = 0,
    val doneBooksCount: Int = 0,
    val deferredBooksCount: Int = 0,
    val serviceDevelopmentBooks: List<BookWithServiceDevelopment> = emptyList(),
    val currentYear: Int = 0
)