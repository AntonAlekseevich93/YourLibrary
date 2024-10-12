package models

import BaseEvent
import main_models.AuthorVo
import main_models.ReadingStatus
import main_models.books.BookShortVo

sealed class BookCreatorEvents : BaseEvent {
    data object ClearUrlEvent : BookCreatorEvents()
    data class OnShowDialogClearAllData(val show: Boolean) : BookCreatorEvents()
    data class SetSelectedBookByMenuClick(val bookId: String) :
        BookCreatorEvents()

    data object ClearSelectedBook : BookCreatorEvents()
    data class ChangeBookReadingStatus(val newStatus: ReadingStatus) : BookCreatorEvents()
    data object ClearAuthorSearch : BookCreatorEvents()
    data object ClearBooksSearch : BookCreatorEvents()
    data object CreateManuallyBook : BookCreatorEvents()
    data class StartUserBookSearchAuthor(val searchedText: String) : BookCreatorEvents()
    data class StartUserBookSearchInAuthorBooks(val searchedText: String) : BookCreatorEvents()
    data object CancelUserBookSearchAuthor : BookCreatorEvents()
    data object CheckIfAuthorIsMatchingAndSetOnCreatedUserScreen : BookCreatorEvents()
    data object ClearMatchesBooksBySelectedAuthors : BookCreatorEvents()
    data class UserBookCreatorAuthorSelected(val author: AuthorVo) : BookCreatorEvents()
    data class UserBookCreatorSetSelectedBook(val book: BookShortVo) : BookCreatorEvents()
    data object UserBookCreatorClearSelectedBook : BookCreatorEvents()
    data class UserBookCreatorChangeReadingStatus(val newStatus: ReadingStatus) :
        BookCreatorEvents()
}