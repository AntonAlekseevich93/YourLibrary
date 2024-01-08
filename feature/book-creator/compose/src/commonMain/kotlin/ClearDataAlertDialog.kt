import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
internal fun ClearDataAlertDialog(
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
) {
    val interactionSource = MutableInteractionSource()
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark)
        ) {
            Column {
                Text(
                    text = Strings.alert_dialog_delete_url_title,
                    modifier = Modifier.padding(16.dp),
                    style = ApplicationTheme.typography.bodyBold,
                    color = ApplicationTheme.colors.mainTextColor,
                )
                Text(
                    text = Strings.alert_dialog_delete_url_description,
                    modifier = Modifier.padding(start = 16.dp),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                )
                Spacer(Modifier.padding(bottom = 20.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row {
                        Text(
                            text = Strings.cancel.uppercase(),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = onDismissRequest
                                ),
                            style = ApplicationTheme.typography.footnoteBold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                        Text(
                            text = Strings.clear.uppercase(),
                            modifier = Modifier
                                .padding(end = 24.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = onClick
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
