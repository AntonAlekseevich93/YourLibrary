package models

import BaseEvent
import androidx.compose.ui.text.input.TextFieldValue
import main_models.books.BookShortVo

sealed class AdminEvents : BaseEvent {
    data object GetBooksForModerating : AdminEvents()
    data object GetBooksForModeratingWithoutUploadingImages : AdminEvents()
    data object ApprovedBook : AdminEvents()
    data object DiscardBook : AdminEvents()
    data object ApprovedWithChangesBook : AdminEvents()
    data object UploadBookCover : AdminEvents()
    data object SetBookAsApprovedWithoutUploadImage : AdminEvents()
    data class SelectBook(val selectedBook: BookShortVo) : AdminEvents()
    class CustomUrlChanged(val url: TextFieldValue) : AdminEvents()
    class ChangeNonModerationStartRange(val startRange: TextFieldValue) : AdminEvents()
    class ChangeNonModerationEndRange(val endRange: TextFieldValue) : AdminEvents()
    class ChangeNeedUseCustomUrl(val needUse: Boolean) : AdminEvents()
    data object ChangeNeedUseHttp : AdminEvents()
    class ChangeNeedUseNonModerationRange(val needUse: Boolean) : AdminEvents()
    data object ChangeSkipImageLongLoadingSettings : AdminEvents()
    data object CloseModerationScreen : AdminEvents()
}