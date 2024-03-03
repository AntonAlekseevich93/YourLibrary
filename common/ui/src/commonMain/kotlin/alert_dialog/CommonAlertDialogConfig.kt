package alert_dialog

import androidx.compose.ui.text.AnnotatedString

class CommonAlertDialogConfig(
    val title: String,
    val description: String = "",
    val descriptionAnnotated: AnnotatedString? = null,
    val acceptButtonTitle: String,
    val dismissButtonTitle: String,
    val showContent: Boolean = false,
)