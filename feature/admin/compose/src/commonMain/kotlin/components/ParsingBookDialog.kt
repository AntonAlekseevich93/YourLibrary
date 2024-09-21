package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ParsingBookDialog(
    onApprove: () -> Unit,
    onDelete: () -> Unit,
    dismiss: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = dismiss,
        modifier = Modifier.clip(
            RoundedCornerShape(12.dp)
        ).background(ApplicationTheme.colors.mainBackgroundWindowDarkColor)
    ) {
        Column {
            Column(
                Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple()
                    ) {
                        onApprove()
                    }) {
                Text(
                    text = "Одобрить книгу",
                    style = ApplicationTheme.typography.headlineMedium,
                    color = ApplicationTheme.colors.mainTextColor,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            Column(
                Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple()
                    ) {
                        onDelete()
                    }) {
                Text(
                    text = "Удалить книгу",
                    style = ApplicationTheme.typography.headlineMedium,
                    color = ApplicationTheme.colors.mainTextColor,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }
}