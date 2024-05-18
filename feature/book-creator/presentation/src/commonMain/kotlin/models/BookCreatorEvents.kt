package models

import BaseEvent
import alert_dialog.CommonAlertDialogConfig
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
    data object ClearAllBookInfo : BookCreatorEvents()
    data class OnShowDialogClearAllData(val show: Boolean) : BookCreatorEvents()
    class OnShowCommonAlertDialog(val config: CommonAlertDialogConfig) : BookCreatorEvents()
    data object DismissCommonAlertDialog : BookCreatorEvents()
    data object SetBookCoverManually : BookCreatorEvents()
}