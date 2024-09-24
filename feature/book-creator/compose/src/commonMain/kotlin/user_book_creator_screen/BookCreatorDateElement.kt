package user_book_creator_screen

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun BookCreatorDateElement(
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
) {
    Row(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)) {
        FilterChip(
            selected = false,
            onClick = onStartDateClick,
            label = {
                Text(
                    "Дата начала чтения",
                    style = ApplicationTheme.typography.bodyBold,
                    color =
                    ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            },
            modifier = Modifier.weight(1f),
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.Transparent,
                selectedContainerColor = Color.Transparent
            ),
            border = BorderStroke(
                1.dp,
                color = ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
            )
        )
        Spacer(Modifier.padding(8.dp))
        FilterChip(
            selected = false,
            onClick = onEndDateClick,
            label = {
                Text(
                    "Дата окончания чтения",
                    style = ApplicationTheme.typography.bodyBold,
                    color =
                    ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            },
            modifier = Modifier.weight(1f),
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.Transparent,
                selectedContainerColor = Color.Transparent
            ),
            border = BorderStroke(
                1.dp,
                color = ApplicationTheme.colors.textFieldColor.unfocusedIndicatorColor,
            )
        )
    }
}