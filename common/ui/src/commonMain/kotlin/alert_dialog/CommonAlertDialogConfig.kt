package alert_dialog

import BaseEvent
import androidx.compose.ui.text.AnnotatedString

data class CommonAlertDialogConfig(
    val title: String,
    val description: String = "",
    val descriptionAnnotated: AnnotatedString? = null,
    val acceptButtonTitle: String,
    val dismissButtonTitle: String,
    val showContent: Boolean = false,
    val acceptEvent: BaseEvent? = null,
    val dismissEvent: BaseEvent? = null,
)