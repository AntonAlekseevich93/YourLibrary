package components.modarations_books_screen.elements

import ApplicationTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AgeRestrictionsElement(ageRestriction: String?, shortView: Boolean) {
    if (!shortView) {
        Row(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
            Text(
                text = "Возрастные ограничения:",
                style = ApplicationTheme.typography.bodyBold,
                color = if (ageRestriction.isNullOrEmpty()) {
                    ApplicationTheme.colors.adminPanelButtons.disapprovedColor
                } else {
                    ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
                }
            )
            ageRestriction?.let {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = it,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }
        }
    }
}