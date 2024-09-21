package components.modarations_books_screen.elements

import ApplicationTheme
import Strings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PagesElement(pages: String, shortView: Boolean) {
    if (!shortView || pages.isEmpty()) {
        Row(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
            Text(
                text = "${Strings.pages_title}:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (pages.isEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = pages,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }
    }
}