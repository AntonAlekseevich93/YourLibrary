package components.modarations_books_screen.elements

import ApplicationTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ChangedBookNameElement(changedName: String) {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Измененное название:",
            style = ApplicationTheme.typography.bodyBold,
            color = if (changedName.isEmpty()) {
                ApplicationTheme.colors.adminPanelButtons.disapprovedColor
            } else {
                ApplicationTheme.colors.adminPanelButtons.uploadColor
            }
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = changedName,
            style = ApplicationTheme.typography.bodyRegular,
            color = ApplicationTheme.colors.mainTextColor
        )
    }
}