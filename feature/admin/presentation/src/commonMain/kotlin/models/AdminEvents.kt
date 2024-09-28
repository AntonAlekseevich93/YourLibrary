package models

import BaseEvent
import androidx.compose.ui.text.input.TextFieldValue
import main_models.books.BookShortVo

sealed class AdminEvents : BaseEvent {
    data class GetRussianBooksForModeration(val coversScreen: Boolean = false) : AdminEvents()
    data class GetEnglishBooksForModeration(val coversScreen: Boolean = false) : AdminEvents()
    data object DiscardBook : AdminEvents()
    data class OnDiscardBookCovers(val book: BookShortVo) : AdminEvents()
    data object SetBookAsApprovedWithoutUploadImage : AdminEvents()
    data object ApproveAllBooks : AdminEvents()
    data class SelectBook(val selectedBook: BookShortVo) : AdminEvents()
    class CustomUrlChanged(val url: TextFieldValue) : AdminEvents()
    class ChangeNonModerationStartRange(val startRange: TextFieldValue) : AdminEvents()
    class ChangeNonModerationEndRange(val endRange: TextFieldValue) : AdminEvents()
    class ChangeNeedUseCustomUrl(val needUse: Boolean) : AdminEvents()
    data object ChangeNeedUseHttp : AdminEvents()
    class ChangeNeedUseNonModerationRange(val needUse: Boolean) : AdminEvents()
    data object OnBack : AdminEvents()
    data object OnChangeBookName : AdminEvents()
    data object OnCancelChangeBookName : AdminEvents()
    data object OnDeleteChangeBookName : AdminEvents()
    data object ClearReviewAndRatingDb : AdminEvents()
    data object ClearAllDb : AdminEvents()
    data object OpenDatabaseMenuScreen : AdminEvents()
    data class OnSaveChangeBookName(val newBookName: String) : AdminEvents()
    class OnParseSingleBook(val url: String) : AdminEvents()
    data object OnOpenModerationScreen : AdminEvents()
    data object OnApproveParsedSingleBook : AdminEvents()
    data object OnClearParsedSingleBookData : AdminEvents()
    data class OnBookSelected(val shortBook: BookShortVo) : AdminEvents()
    data class SendTestNotification(val title: String, val body: String) : AdminEvents()
}