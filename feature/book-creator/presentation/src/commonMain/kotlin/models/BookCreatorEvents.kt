package models

import BaseEvent
import androidx.compose.ui.text.input.TextFieldValue

sealed class BookCreatorEvents : BaseEvent {
    data object GoBack : BookCreatorEvents()
    data object CreateBookEvent : BookCreatorEvents()
    class UrlTextChangedEvent(val urlTextFieldValue: TextFieldValue) : BookCreatorEvents()
    data object ClearUrlEvent : BookCreatorEvents()
    data object OnFinishParsingUrl : BookCreatorEvents()
    data object OnCreateBookManuallyEvent : BookCreatorEvents()
    data object DisableCreateBookManuallyEvent : BookCreatorEvents()
    data object OnClearUrlAndCreateBookManuallyEvent : BookCreatorEvents()
    data object ClearAllAuthorInfo : BookCreatorEvents()
    data class OnShowDialogClearAllData(val show: Boolean) : BookCreatorEvents()
    data object DismissCommonAlertDialog : BookCreatorEvents()
    data object SetBookCoverManually : BookCreatorEvents()
}