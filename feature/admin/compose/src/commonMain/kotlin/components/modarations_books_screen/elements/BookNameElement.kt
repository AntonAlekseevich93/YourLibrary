package components.modarations_books_screen.elements

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.AdminEvents

@Composable
internal fun BaseEventScope<BaseEvent>.BookNameElement(bookName: String) {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Название:",
            style = ApplicationTheme.typography.bodyBold,
            color = if (bookName.isEmpty()) {
                ApplicationTheme.colors.adminPanelButtons.disapprovedColor
            } else {
                ApplicationTheme.colors.adminPanelButtons.approvedWithChangesColor
            }
        )
        Text(
            modifier = Modifier.clickable {
                sendEvent(AdminEvents.OnChangeBookName)
            }.padding(start = 8.dp),
            text = bookName,
            style = ApplicationTheme.typography.bodyBold,
            color = ApplicationTheme.colors.mainTextColor
        )
    }
}