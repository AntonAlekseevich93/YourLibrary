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
internal fun AuthorElement(authorName: String, shortView: Boolean) {
    if (!shortView || authorName.isEmpty()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Автор:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (authorName.isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = authorName,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }
    }
}