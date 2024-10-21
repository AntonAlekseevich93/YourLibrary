package reading_status

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import main_models.ReadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingStatusSelectorDialog(
    currentStatus: ReadingStatus?,
    useDivider: Boolean = false,
    selectStatusListener: (selectedStatus: ReadingStatus) -> Unit,
    dismiss: () -> Unit
) {
    val statuses = remember { ReadingStatus.entries }
    BasicAlertDialog(
        onDismissRequest = dismiss,
        modifier = Modifier.clip(
            RoundedCornerShape(12.dp)
        ).background(ApplicationTheme.colors.mainBackgroundWindowDarkColor)
    ) {
        Column {
            statuses.fastForEachIndexed { index, status ->
                val backgroundColor: Color = if (currentStatus == null) Color.Transparent
                else if (status.id == currentStatus.id) currentStatus.getStatusColor() else Color.Transparent
                val textColor: Color =
                    if (currentStatus == null) ApplicationTheme.colors.mainTextColor
                    else if (status.id == currentStatus.id) Color.Black else ApplicationTheme.colors.mainTextColor
                Box(
                    Modifier
                        .background(backgroundColor)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple()
                        ) {
                            selectStatusListener(status)
                        }) {
                    Text(
                        text = status.nameValue,
                        style = ApplicationTheme.typography.headlineMedium,
                        color = textColor,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }
                if (useDivider && index != statuses.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = ApplicationTheme.colors.dividerLight
                    )
                }
            }
        }
    }
}