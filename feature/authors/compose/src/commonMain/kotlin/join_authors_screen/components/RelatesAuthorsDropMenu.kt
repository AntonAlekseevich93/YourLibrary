package join_authors_screen.components

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.AuthorVo
import models.AuthorsEvents

@Composable
fun BaseEventScope<BaseEvent>.RelatesAuthorsDropMenu(
    modifiedAuthor: AuthorVo,
    originalAuthor: AuthorVo,
    onClose: () -> Unit,
    showAlertDialog: (config: CommonAlertDialogConfig, newAuthorVo: AuthorVo) -> Unit,
) {
    val interactionSource = MutableInteractionSource()
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = Strings.remove_from_relates,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp).clickable {
                this@RelatesAuthorsDropMenu.sendEvent(
                    AuthorsEvents.RemoveAuthorFromRelates(
                        originalAuthor = originalAuthor,
                        modifiedAuthorId = modifiedAuthor.id
                    )
                )
                onClose.invoke()
            }
        )
        Text(
            text = Strings.as_main_author,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp)
                .clickable(interactionSource = interactionSource, indication = null) {
                    showAlertDialog.invoke(
                        CommonAlertDialogConfig(
                            title = Strings.as_main_author_alert_dialog_title,
                            description = Strings.as_main_author_alert_dialog_description.format(
                                modifiedAuthor.name,
                                originalAuthor.name
                            ),
                            acceptButtonTitle = Strings.as_main_author_alert_dialog_accept_button.format(
                                originalAuthor.name
                            ),
                            dismissButtonTitle = Strings.cancel
                        ),
                        originalAuthor
                    )
                }
        )
        Text(
            text = Strings.rename,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp)
                .clickable(interactionSource = interactionSource, indication = null) {
                    showAlertDialog.invoke(
                        CommonAlertDialogConfig(
                            title = Strings.change_author_name,
                            acceptButtonTitle = Strings.rename,
                            dismissButtonTitle = Strings.cancel,
                            showContent = true
                        ),
                        originalAuthor
                    )
                }
        )
        Text(
            text = Strings.delete,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding()
        )
    }
}