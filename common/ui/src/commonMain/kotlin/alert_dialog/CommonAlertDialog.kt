package alert_dialog

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonAlertDialog(
    config: CommonAlertDialogConfig,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
    acceptListener: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val interactionSource = MutableInteractionSource()
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark)
        ) {
            Column {
                Text(
                    text = config.title,
                    modifier = Modifier.padding(16.dp),
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.mainTextColor,
                )
                if (config.descriptionAnnotated != null) {
                    Text(
                        text = config.descriptionAnnotated,
                        modifier = Modifier.padding(start = 16.dp),
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                } else if (config.description.isNotEmpty()) {
                    Text(
                        text = config.description,
                        modifier = Modifier.padding(start = 16.dp),
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                }

                if (config.showContent) {
                    content?.invoke()
                }

                Spacer(Modifier.padding(bottom = 20.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row {
                        Text(
                            text = config.acceptButtonTitle.uppercase(),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = acceptListener

                                ),
                            style = ApplicationTheme.typography.footnoteBold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                        Text(
                            text = config.dismissButtonTitle.uppercase(),
                            modifier = Modifier
                                .padding(end = 24.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = onDismissRequest
                                ),
                            style = ApplicationTheme.typography.footnoteBold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                    }
                }
            }
        }
    }
}
