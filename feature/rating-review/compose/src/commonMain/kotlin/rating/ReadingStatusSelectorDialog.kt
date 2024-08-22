package rating

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import main_models.ReadingStatus
import reading_status.getStatusColor

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
        ).background(ApplicationTheme.colors.mainBackgroundColor)
    ) {
        Column {
            statuses.fastForEachIndexed { index, status ->
                val color: Color = if (currentStatus == null) Color.Transparent
                else if (status.id == currentStatus.id) currentStatus.getStatusColor() else Color.Transparent
                Box(
                    Modifier
                        .background(color)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple()
                        ) {
                            selectStatusListener(status)
                        }) {
                    Text(
                        text = status.nameValue,
                        style = ApplicationTheme.typography.headlineMedium,
                        color = ApplicationTheme.colors.mainTextColor,
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